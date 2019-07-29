package com.spring.Demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.spring.Demo.GoogleAuthorizeUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.spring.Demo.ReadFile;

@SpringBootApplication
@RestController
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/")
	public String hello() {
		
		return "hello world!";
	}

	@GetMapping("/test")
	public String test() {

		return System.getProperty("user.home") + "--" + System.getProperty("user.dir");
	}

	@GetMapping("/getUrlAuthorize")
	public String test2() {
		URL sqlScriptUrl = DemoApplication.class
                .getClassLoader().getResource("temp.txt");
		List<String> rs = ReadFile.readFile(sqlScriptUrl.getPath());
		String temp = "";
		if (rs != null) {
			for (int i = 0; i < rs.size(); i++) {
				temp += rs.get(i) + "\n";
			}
		}
		return temp;
	}

	@GetMapping("/getSheet")
	public String getSheet() throws IOException, GeneralSecurityException {
		GoogleAuthorizeUtil google = new GoogleAuthorizeUtil();
		Sheets service = google.getSheetsService();

		// Prints the names and majors of students in a sample spreadsheet:
		// https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
		String temp = "";
		String spreadsheetId = "1I-575yBsH2xoAcEebKAvW19fo0KbO00xaG1ljxk2AEg";
		String range = "A1:E";
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		List<List<Object>> values = response.getValues();
		if (values == null || values.size() == 0) {
			System.out.println("No data found.");
		} else {
			System.out.println("Name, Major");
			for (List row : values) {
				// Print columns A and E, which correspond to indices 0 and 4.
				System.out.printf("%s, %s\n", row.get(0), row.get(0));
				temp = row.get(0).toString();
			}
		}
		return "hello world!" + temp;
	}
	
	@RequestMapping(value = "/cmd", method = RequestMethod.GET)
    public String greeding(@RequestParam(value = "cmd", required = true) String cmd) {
        String output = "";
        try {
            output = executeCommand(cmd);
            return output;
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }

    }
	
	public String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

}
