package com.hpe.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadPuppetConfigController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6282669056643441832L;

	private static final String PUPPET_CONFIG_PARAM = "puppetConfigFile";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileName = getServletContext().getInitParameter(PUPPET_CONFIG_PARAM);
		List<String> list = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			list = stream
					.filter(line -> line.trim().startsWith("Apache Log4j"))
					.collect(Collectors.toList());
			
			request.setAttribute("logVersion", list.get(0).trim().split(" ")[2]);
			
			stream.close();

		} catch (IOException e) {
			
			request.setAttribute("errorMessage", "No Apache Log4j installed.");
			
			e.printStackTrace();
		}
		
		RequestDispatcher view = request.getRequestDispatcher("index.jsp");
		view.forward(request, response);
		
	}

	
	
}
