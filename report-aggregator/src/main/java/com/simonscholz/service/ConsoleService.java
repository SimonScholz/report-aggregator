package com.simonscholz.service;

import java.io.PrintStream;

import org.springframework.stereotype.Service;

@Service
public class ConsoleService {

	private final static String ANSI_BLUE = "\u001B[33m";
	private final static String ANSI_RESET = "\u001B[0m";

	private final PrintStream out = System.out;

	public void write(String msg, Object... args) {
		this.out.print("> ");
		this.out.print(ANSI_BLUE);
		this.out.printf(msg, args);
		this.out.print(ANSI_RESET);
		this.out.println();
	}
}