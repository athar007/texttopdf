package com.fujitsu.texttopdf.controller;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fujitsu.texttopdf.FileUploadUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class TextToPdfController {
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@PostMapping("/changefiletype")
	public String changeFileType(@RequestParam("upload") MultipartFile file, Model m) {
		String url = "";
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
 
        String uploadDir = "D:\\java\\Spring\\boot\\SpringTextToPdf\\src\\main\\resources\\static\\";
        try {
        	//System.out.println("FileUploadUtil ");
        	FileUploadUtil.saveFile(uploadDir+"uploads\\", fileName, file);
        } catch(IOException ie) {
        	System.out.println("Error! file upload "+ie.getMessage());
        }
		
		try {
			//System.out.println("FileInputStream");
			Document pdfDoc = new Document(PageSize.A4);
			String[] fileNamearr = fileName.split("\\.");
			PdfWriter.getInstance(pdfDoc, new FileOutputStream(uploadDir+"pdf\\"+fileNamearr[0]+".pdf"))
			  .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
			pdfDoc.open();
			Font myfont = new Font();
			myfont.setStyle(Font.NORMAL);
			myfont.setSize(11);
			pdfDoc.add(new Paragraph("\n"));
			BufferedReader br = new BufferedReader(new FileReader(uploadDir+"uploads\\"+fileName));
			
			String strLine;
			while ((strLine = br.readLine()) != null) {
			    Paragraph para = new Paragraph(strLine + "\n", myfont);
			    para.setAlignment(Element.ALIGN_JUSTIFIED);
			    pdfDoc.add(para);
			}
			//System.out.println(pdfDoc);
			url=uploadDir+"pdf\\"+fileNamearr[0]+".pdf";
			System.out.println("Pdf Converted Sucessfully!");
			m.addAttribute("downloadurl", "Converted Sucessfully!");
			pdfDoc.close();
			br.close();
			return "redirect:/";
			
		}
		catch(Exception fe) {
			System.out.println("Error! file stream "+fe.getMessage());
			return "redirect:/";
			
		}
		
		//System.out.println(file.getOriginalFilename());
		//return "redirect:/";
	}

}
