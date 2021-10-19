package com.anas.readpackagesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "com.anas.readpackagesapp.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.title_activity_main);
        Log.v(TAG, "Activity created successfully");

        TextView textView = findViewById(R.id.text);
        String[] commandsExecute = { "su", "-c", "cat /data/system/packages.xml"};

        try {

            Process process = Runtime.getRuntime().exec(commandsExecute);

            //Execute and read the results of the command
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            String s = null;
            StringBuilder result = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                result.append(s);
            }

            //Turn contents of packages.xml into String
            String resultString = result.toString();

            InputSource resultToParse = new InputSource(new StringReader(resultString));

            //parsing the XML output using org.w3c.dom library
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(resultToParse);
            doc.getDocumentElement().normalize();

            NodeList packageNode = doc.getElementsByTagName("package");

            StringBuilder packageStringBuilder = new StringBuilder();

            packageStringBuilder.append("Number of packages: " + packageNode.getLength() + "\n \n");

            for (int i = 0; i < packageNode.getLength(); i++) {
                Node node = packageNode.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // get package name attribute
                    String packageName = element.getAttribute("name");
                    // get package codePath attribute
                    String packageCodePath = element.getAttribute("codePath");

                    packageStringBuilder.append("name: " + packageName);
                    packageStringBuilder.append("\n");
                    packageStringBuilder.append("codePath: " + packageCodePath);
                    packageStringBuilder.append("\n");
                    packageStringBuilder.append("--------------------------------------------------");
                    packageStringBuilder.append("\n");

                }
            }

            //String to display in the ui
            String packages = packageStringBuilder.toString();

            textView.setText(packages);

            Log.v(TAG, "Activity Runtime exec");
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

}