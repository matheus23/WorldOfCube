/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.worldOfCube.Log;
import org.worldOfCube.server.gui.ServerFrame;
import org.worldOfCube.server.gui.chat.ChatPane;
import org.worldOfCube.server.gui.viewer.ServerPane;

public class ServerHandler implements ActionListener, Printer {

	private static String commandChar = "#";
	private static int STD_PORT = 5455;

	private List<Command> commands = new ArrayList<Command>();

	private ChatPane cp;
	private ServerPane sp;
	private ServerFrame sf;
	private ServerListener sl;

	public ServerHandler(ChatPane cp, ServerPane sp, ServerFrame sf) {
		this.cp = cp;
		this.sp = sp;
		this.sf = sf;
		sl = new ServerListener(this);
		cp.getChatInput().setActionListener(this);
		addAllCommands();
		ChatPrintStream cps = new ChatPrintStream(this);
		Log.setOut(cps);
		Log.setErr(cps);
	}

	public void println(String prefix, String s) {
		cp.getChatText().append(String.format("(%8s): %s", prefix, s));
		println();
	}

	public void println(Printer p, String s) {
		print(p, s);
		println();
	}

	private void print(Printer p, String s) {
		cp.getChatText().append(String.format("(%8s): %s", p.getPrefix(), s));
	}

	public void println() {
		cp.getChatText().append("\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		processInput(command);
	}

	public void processInput(String input) {
		println(this, input);

		if (input.startsWith(commandChar)) {
			String command = input.substring(1);
			while(command.startsWith(" ")) {
				command = command.substring(1);
			}
			handleCommand(command);
		}
	}

	public void handleCommand(String command) {
		String[] args = command.split(" ");
		int num = 0;
		for (int i = 0; i < commands.size(); i++) {
			Command cmd = commands.get(i);
			if (cmd.getArguments()+1 == args.length
					&& cmd.getCommand().equals(args[0])) {
				cmd.handleCommand(args);
				num++;
			}
		}
		if (num == 0) {
			println(this, "No command could be found for " + command);
		}
	}

	private void addAllCommands() {
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				stop();
			}

			@Override
			public String getCommand() {
				return "exit";
			}

			@Override
			public int getArguments() {
				return 0;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				sf.screenshot();
				println("COMMAND", "Took screenshot.");
			}

			@Override
			public String getCommand() {
				return "screenshot";
			}

			@Override
			public int getArguments() {
				return 0;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				try {
					int port = Integer.parseInt(str[1]);
					if (port < 0 || port > 65535) {
						println("COMMAND", "Unsupported port number: " + str[1]);
						return;
					}
					sl.start(port);
				} catch (NumberFormatException e) {
					println("COMMAND", "Unsupported port number: " + str[1]);
				}
			}

			@Override
			public String getCommand() {
				return "start-server";
			}

			@Override
			public int getArguments() {
				return 1;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				sl.start(STD_PORT);
			}

			@Override
			public String getCommand() {
				return "start-server";
			}

			@Override
			public int getArguments() {
				return 0;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				sl.stop();
			}

			@Override
			public String getCommand() {
				return "stop-server";
			}

			@Override
			public int getArguments() {
				return 0;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				Random rand = new Random();
				int random =  rand.nextInt();
				String seed = Integer.toString(random);
				sp.getWorldViewer().generate(seed.hashCode());
				println("COMMAND", "Regenerating world with seed " + seed);
			}

			@Override
			public String getCommand() {
				return "regenerate";
			}

			@Override
			public int getArguments() {
				return 0;
			}
		});
		commands.add(new Command() {
			@Override
			public void handleCommand(String[] str) {
				sp.getWorldViewer().generate(str[1].hashCode());
				println("COMMAND", "Regenerating world with seed " + str[1]);
			}

			@Override
			public String getCommand() {
				return "regenerate";
			}

			@Override
			public int getArguments() {
				return 1;
			}
		});
	}

	public void stop() {
		sl.stop();
		sf.dispose();
		cp.stop();
		sp.stop();
	}

	@Override
	public String getPrefix() {
		return "SERVER";
	}

	private class ChatPrintStream extends PrintStream {

		public ChatPrintStream(final ServerHandler sh) {
			super(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
					byte[] bytes = new byte[1];
					bytes[0] = (byte)b;
					sh.cp.getChatText().append(new String(bytes));
				}
			});
		}
	}

}
