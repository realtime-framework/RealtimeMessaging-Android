package ibt.ortc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpException;

public class RestWebservice {
  
  protected static void getAsync(URL url, OnRestWebserviceResponse callback) {
    requestAsync(url, "GET", null, callback);
  }

  protected static void postAsync(URL url, String content, OnRestWebserviceResponse callback) {
    requestAsync(url, "POST", content, callback);
  }

  private static void requestAsync(final URL url, final String method, final String content, final OnRestWebserviceResponse callback) {
    Runnable task = new Runnable() {

      @Override
      public void run() {
        if (method.equals("GET")) {
          try {
            String result = "https".equals(url.getProtocol()) ? secureGetRequest(url) : unsecureGetRequest(url);
            callback.run(null, result);
          } catch (IOException error) {
            callback.run(error, null);
          } catch (HttpException error) {
            callback.run(error, null);
          }
        } else if (method.equals("POST")) {
          try {
            String result = "https".equals(url.getProtocol()) ? securePostRequest(url, content) : unsecurePostRequest(url, content);
            callback.run(null, result);
          } catch (IOException error) {
            callback.run(error, null);
          } catch (HttpException error) {
            callback.run(error, null);
          }
        } else {
          callback.run(new Exception(String.format("Invalid request method - %s", method)), null);
        }
      }
    };

    new Thread(task).start();
  }

  private static String unsecureGetRequest(URL url) throws IOException, HttpException {
    HttpURLConnection connection = null;
    String result = "";

    try {
      connection = (HttpURLConnection) url.openConnection();
      InputStream responseBody;
      if (connection.getResponseCode() != 200 && connection.getResponseCode() != -1) {
        responseBody = connection.getErrorStream();

        result = readResponseBody(responseBody);
        throw new HttpException(result);
      } else {
        responseBody = connection.getInputStream();

        result = readResponseBody(responseBody);
      }
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return result;
  }

  // CAUSE: Prefer throwing/catching meaningful exceptions instead of
  // Exception
  private static String secureGetRequest(URL url) throws IOException, HttpException {
    HttpsURLConnection connection = null;
    StringBuilder result = new StringBuilder(16);

    // connection.setDoOutput(true);

    try {
      connection = (HttpsURLConnection) url.openConnection();
      BufferedReader rd = null;

      try {
        if (connection.getResponseCode() != 200) {
          // CAUSE: Reliance on default encoding
          rd = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {            
            result.append(line);
            line = rd.readLine();
          }
          rd.close();
          throw new HttpException(result.toString());
        } else {
          rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {
            result.append(line);
            line = rd.readLine();
          }

        }
        // CAUSE: Method may fail to close stream on exception
      } finally {
        if (rd != null) {
          rd.close();
        }
      }
      // CAUSE: Method may fail to close connection on exception
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return result.toString();
  }

  private static String unsecurePostRequest(URL url, String postBody) throws IOException, HttpException {
    HttpURLConnection connection = null;
    StringBuilder result = new StringBuilder(16);

    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      OutputStreamWriter wr = null;
      try {
        // CAUSE: Reliance on default encoding
        wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

        wr.write(postBody);

        wr.flush();
        // CAUSE: Method may fail to close stream on exception
      } finally {
        if (wr != null) {
          wr.close();
        }
      }

      BufferedReader rd = null;
      try {
    	  if (connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
          // CAUSE: Reliance on default encoding
          rd = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {
            // CAUSE: Method concatenates strings using + in a loop
            result.append(line);
            line = rd.readLine();
          }

          throw new HttpException(result.toString());
        } else {
          // CAUSE: Reliance on default encoding
          rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {
            // CAUSE: Method concatenates strings using + in a loop
            result.append(line);
            line = rd.readLine();
          }
        }
        // CAUSE: Method may fail to close stream on exception
      } finally {
        if (rd != null) {
          rd.close();
        }
      }
      // CAUSE: Method may fail to close connection on exception
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return result.toString();
  }

  private static String securePostRequest(URL url, String postBody) throws IOException, HttpException {
    HttpsURLConnection connection = null;
    // TODO: specify a correct capacity
    StringBuilder result = new StringBuilder(16);

    try {
      connection = (HttpsURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      OutputStreamWriter wr = null;
      try {
        // CAUSE: Reliance on default encoding
        wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

        wr.write(postBody);

        wr.flush();
        // CAUSE: Method may fail to close stream on exception
      } finally {
        if (wr != null) {
          wr.close();
        }
      }

      BufferedReader rd = null;
      try {
        if (connection.getResponseCode() < 200 || connection.getResponseCode() > 299) {
          // CAUSE: Reliance on default encoding        	
          rd = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {
            // CAUSE: Method concatenates strings using + in a loop
            result.append(line);
            line = rd.readLine();
          }

          throw new HttpException(result.toString());
        } else {
          // CAUSE: Reliance on default encoding
          rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
          String line = rd.readLine();
          while (line != null) {
            // CAUSE: Method concatenates strings using + in a loop
            result.append(line);
            line = rd.readLine();
          }
        }
        // CAUSE: Method may fail to close stream on exception
      } finally {
        if (rd != null) {
          rd.close();
        }
      }
      // CAUSE: Method may fail to close connection on exception
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return result.toString();
  }

  private static String readResponseBody(InputStream responseBody) throws IOException {
    // TODO: specify a correct capacity
    StringBuilder result = new StringBuilder(16);

    if (responseBody != null) {
      BufferedReader rd = null;

      try {
        // CAUSE: Reliance on default encoding
        rd = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
        String line = rd.readLine();
        // CAUSE: Assignment expressions nested inside other expressions
        while (line != null) {
          // CAUSE: Method concatenates strings using + in a loop
          result.append(line);
          line = rd.readLine();
        }
      } catch (IOException e) {
        result = new StringBuilder(e.getMessage());
      } finally {
        if (rd != null) {
          rd.close();
        }
      }
    }

    return result.toString();
  }

  private RestWebservice() {
  }
}
