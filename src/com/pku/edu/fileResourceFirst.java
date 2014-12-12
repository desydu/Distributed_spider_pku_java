package pku.eu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.output.FileWriterWithEncoding;

public class fileResource {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        int i = 1;
        String path = "rule2";
        File file = new File(path + ".txt");
        File newfile = new File("rules1.txt");
        newfile.createNewFile();
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        BufferedWriter pw = new BufferedWriter(new FileWriterWithEncoding(newfile, "UTF-8", true));
        String temp = null;
        while ((temp = br.readLine()) != null) {
            i++;
            System.out.println(temp);
            //pw.append(temp);
            pw.write(temp);
            pw.newLine();
        }
        pw.flush();
        System.out.println(i);
    }

}
