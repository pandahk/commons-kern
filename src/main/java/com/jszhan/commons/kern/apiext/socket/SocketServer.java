package com.jszhan.commons.kern.apiext.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

public class SocketServer {

	public static void main(String[] args) throws Exception {
		server();
//		valid("12");
		System.out.println(123);
		System.out.println(123);
		System.out.println(123);
	}

	private static void server() throws IOException {
		/**
		 * 基于TCP协议的Socket通信，实现用户登录，服务端
		 */
		// 1、创建一个服务器端Socket，即ServerSocket，指定绑定的端口，并监听此端口
		ServerSocket serverSocket = new ServerSocket(10086);// 1024-65535的某个端口
		ExecutorService ex=Executors.newFixedThreadPool(100);
		while (true) {
			// 2、调用accept()方法开始监听，等待客户端的连接
			System.out.println("--begin..");
			final Socket socket = serverSocket.accept();
			System.out.println("--0101..");
			ex.execute(new Runnable() {
				
				@Override
				public void run() {
					// 3、获取输入流，并读取客户端信息
					InputStream is;
					try {
						is = socket.getInputStream();
					
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String info = null;
					while ((info = br.readLine()) != null) {
						System.out.println("Hello,我是服务器，客户端说：" + info);
					}
					socket.shutdownInput();// 关闭输入流
					// 4、获取输出流，响应客户端的请求
					OutputStream os = socket.getOutputStream();
					PrintWriter pw = new PrintWriter(os);
					pw.write("Hello World！");
					pw.flush();

					// 5、关闭资源
					pw.close();
					os.close();
					br.close();
					isr.close();
					is.close();
					socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			});
			
		}
	}

	
	public static void valid(String name){
		if (StringUtils.isEmpty(name)) {
			throw new RuntimeException();
		}else{
			return ;
		}
		
		
	}
}
