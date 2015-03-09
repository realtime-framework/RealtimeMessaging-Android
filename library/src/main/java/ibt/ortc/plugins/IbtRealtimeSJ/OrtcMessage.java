/**
 *
 */
package ibt.ortc.plugins.IbtRealtimeSJ;

import ibt.ortc.api.Strings;


//import ibt.ortc.extensibility.CharEscaper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class OrtcMessage {
    private static final String OPERATION_PATTERN = "^a\\[\"\\{\\\\\"op\\\\\":\\\\\"([^\"]+)\\\\\",(.*)\\}\"\\]$";
    private static final String CHANNEL_PATTERN = "^\\\\\"ch\\\\\":\\\\\"(.*)\\\\\"$";
    private static final String RECEIVED_PATTERN = "^a?\\[\"\\{\\\\\"ch\\\\\":\\\\\"(.*)\\\\\",\\\\\"m\\\\\":\\\\\"([\\s\\S]*?)\\\\\"\\}\"\\]$";
    private static final String MULTI_PART_MESSAGE_PATTERN = "^(.[^_]*)_(.[^-]*)-(.[^_]*)_([\\s\\S]*?)$";
    private static final String EXCEPTION_PATTERN = "^\\\\\"ex\\\\\":(\\{.*\\})$";
    private static final String PERMISSIONS_PATTERN = "^\\\\\"up\\\\\":{1}(.*),\\\\\"set\\\\\":(.*)$";
    private static final String CLOSE_PATTERN = "^c\\[\\d\\d\\d\\d,\"([\\s\\S])*\"]$";

    private static Pattern operationPattern;
    private static Pattern subscribedPattern;
    private static Pattern receivedPattern;
    private static Pattern multipartMessagePattern;
    private static Pattern unsubscribedPattern;
    private static Pattern exceptionPattern;
    private static Pattern permissionsPattern;
    private static Pattern closePattern;

    private OrtcOperation operation;
    private String message;
    private String messageChannel;
    private String messageId;
    private int messagePart;
    private int messageTotalParts;
    private static final Hashtable<String, OrtcOperation> operationIndex = new Hashtable<String, OrtcOperation>(11);
    private static final Hashtable<String, OrtcServerErrorException.OrtcServerErrorOperation> errorOperationIndex = new Hashtable<String, OrtcServerErrorException.OrtcServerErrorOperation>(11);

    static {
            operationPattern = Pattern.compile(OPERATION_PATTERN);
            subscribedPattern = Pattern.compile(CHANNEL_PATTERN);
            receivedPattern = Pattern.compile(RECEIVED_PATTERN);
            multipartMessagePattern = Pattern.compile(MULTI_PART_MESSAGE_PATTERN);
            unsubscribedPattern = Pattern.compile(CHANNEL_PATTERN);
            exceptionPattern = Pattern.compile(EXCEPTION_PATTERN);
            permissionsPattern = Pattern.compile(PERMISSIONS_PATTERN);
            closePattern = Pattern.compile(CLOSE_PATTERN);

        operationIndex.put("ortc-validated", OrtcOperation.Validated);
        operationIndex.put("ortc-subscribed", OrtcOperation.Subscribed);
        operationIndex.put("ortc-unsubscribed", OrtcOperation.Unsubscribed);
        operationIndex.put("ortc-error", OrtcOperation.Error);

        errorOperationIndex.put("ex", OrtcServerErrorException.OrtcServerErrorOperation.Unexpected);
        errorOperationIndex.put("validate", OrtcServerErrorException.OrtcServerErrorOperation.Validate);
        errorOperationIndex.put("subscribe", OrtcServerErrorException.OrtcServerErrorOperation.Subscribe);
        errorOperationIndex.put("subscribe_maxsize", OrtcServerErrorException.OrtcServerErrorOperation.Subscribe_MaxSize);
        errorOperationIndex.put("unsubscribe_maxsize", OrtcServerErrorException.OrtcServerErrorOperation.Unsubscribe_MaxSize);
        errorOperationIndex.put("send_maxsize", OrtcServerErrorException.OrtcServerErrorOperation.Send_MaxSize);
    }

    public OrtcMessage(OrtcOperation operation, String message, String messageChannel, String messageId, int messagePart, int messageTotalParts) {
        this.operation = operation;
        this.message = message;
        this.messageChannel = messageChannel;
        this.messageId = messageId;
        this.messagePart = messagePart;
        this.messageTotalParts = messageTotalParts;
    }

    // CAUSE: Prefer throwing/catching meaningful exceptions instead of Exception
    public static OrtcMessage parseMessage(String message) throws IOException {
        OrtcOperation operation = null;
        String parsedMessage = null;
        String messageChannel = null;
        String messageId = null;
        int messagePart = -1;
        int messageTotalParts = -1;

        Matcher matcher = operationPattern.matcher(message);

        if (matcher != null && !matcher.matches()) {
            //matcher = receivedPattern.matcher(message.replace("\\\"", "\""));
			matcher = receivedPattern.matcher(message);
			
            if (matcher != null && matcher.matches()) {
                operation = OrtcOperation.Received;
                parsedMessage = matcher.group(2);
                messageChannel = matcher.group(1);

                //parsedMessage = parsedMessage.replace("\\\\n", "\n").replace("\\\"", "\"").replace("\\\\\\\\", "\\");

                Matcher multiPartMatcher = parseMultiPartMessage(parsedMessage);

                try{
                    if (multiPartMatcher.matches()) {
                        parsedMessage = multiPartMatcher.group(4);
                        messageId = multiPartMatcher.group(1);
                        messagePart = Strings.isNullOrEmpty(multiPartMatcher.group(2)) ? -1 : Integer.parseInt(multiPartMatcher.group(2));
                        messageTotalParts = Strings.isNullOrEmpty(multiPartMatcher.group(3)) ? -1 : Integer.parseInt(multiPartMatcher.group(3));
                    }
                }catch(NumberFormatException parseException){
                    //throw new NumberFormatException("Invalid message format: " + message + " - Error " + parseException.toString());
                    parsedMessage = matcher.group(2);
                    messageId = null;
                    messagePart = -1;
                    messageTotalParts = -1;
                }
            } else {
                matcher = closePattern.matcher(message);
                if (matcher != null && matcher.matches()){
                    operation = OrtcOperation.Close;
                }
                else {
                    throw new IOException(String.format("Invalid message format: %s", message));
                }
            }
        // CAUSE: Possible null pointer dereference
        } else {
            operation = operationIndex.get(matcher.group(1));
            parsedMessage = matcher.group(2);
        }

        return new OrtcMessage(operation, parsedMessage, messageChannel, messageId, messagePart, messageTotalParts);
    }

    private static Matcher parseMultiPartMessage(String message) {
        Matcher result = multipartMessagePattern.matcher(message);

        return result;
    }

    public int getSessionExpirationTime() {
        int result = 0;

        Matcher matcher = permissionsPattern.matcher(this.message);

        if (matcher != null && matcher.matches()) {
            String content = matcher.group(2);

            if (!Strings.isNullOrEmpty(content)) {
                result = Integer.parseInt(content);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    // TODO: Hashtable is an absolete Collection
    public Hashtable<String, String> getPermissions() {
        // CAUSE: Instantiating collection without specified initial capacity
        // TODO: Hashtable is an absolete Collection
        Hashtable<String, String> result = new Hashtable<String, String>(11);

        Matcher matcher = permissionsPattern.matcher(this.message);

        if (matcher != null && matcher.matches()) {
            String content = matcher.group(1);

			content = JSONValue.parse("\""+content+"\"").toString();
            JSONObject json = (JSONObject) (JSONValue.parse(content));

            if (json != null) {
                Iterator<?> iter = ((Map<String, OrtcOperation>) json).entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();

                    // CAUSE: Redundant cast
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return result;
    }

    public String channelSubscribed() throws Exception {
        Matcher matcher = subscribedPattern.matcher(this.message);

        if (!matcher.matches()) {
            // TODO: create a custom exception
            throw new Exception("Subscribe channel match to channel not found");
        }

        String result = matcher.group(1);

        return result;
    }

    public String channelUnsubscribed() throws Exception {
        Matcher matcher = unsubscribedPattern.matcher(this.message);

        if (!matcher.matches()) {
            // TODO: create a custom exception
            throw new Exception("Unsubscribe channel match to channel not found");
        }

        String result = matcher.group(1);

        return result;
    }

    public OrtcServerErrorException serverError() throws Exception {
        Matcher matcher = exceptionPattern.matcher(this.message);

        if (!matcher.matches()) {
            // TODO: create a custom exception
            throw new Exception("Exception match not found");
        }

        String content = matcher.group(1).replace("\\\"", "\"");        

        JSONObject json = (JSONObject) (JSONValue.parse(content));

        String errorMessage = (String)(json.get("ex"));
        String errorOperation = (String)(json.get("op"));
        String channel = (String)(json.get("ch"));

        OrtcServerErrorException.OrtcServerErrorOperation op = errorOperationIndex.get(errorOperation);

        return new OrtcServerErrorException(op,channel,errorMessage);
    }

    public OrtcOperation getOperation() {
        return operation;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageChannel() {
        return this.messageChannel;
    }

    public String getMessageId() {
        return messageId;
    }

    public int getMessagePart() {
        return messagePart;
    }

    public int getMessageTotalParts() {
        return messageTotalParts;
    }
}
