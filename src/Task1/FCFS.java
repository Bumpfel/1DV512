/*
 * File:	Process.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2018
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class FCFS{

	// The list of processes to be scheduled
	public ArrayList<Process> processes;

	// Class constructor
	public FCFS(ArrayList<Process> processes) {
		this.processes = processes;
	}

	public void run() {
		processes.sort((s1, s2) -> s1.getArrivalTime() - s2.getArrivalTime());
		Queue<Process> queue = new LinkedList<>(processes);
		
		int time = 0;
		int prevTime = 0;
		Process active = null;
		boolean done = false;
		while(!done) {
			if(active == null && time >= queue.peek().getArrivalTime()) {
				active = queue.poll();
				prevTime = time;
			}
			time ++;
			if(active != null && prevTime + active.getBurstTime() == time) {
				active.setCompletedTime(time);
				active.setTurnaroundTime(active.getCompletedTime() - active.getArrivalTime());
				active.setWaitingTime(active.getTurnaroundTime() - active.getBurstTime());
				active = null;
				
				if(queue.isEmpty())
					done = true;
			}			
		}

		printGanttChart();
		printTable();
		System.out.println("\n\n\n");
	}

	public void printTable() {
		String[] cols = { "PID", "AT", "BT", "CT", "TAT", "WT" };
		System.out.println("-------------------------------------");
		for(String col : cols) {
			System.out.format("%-7s", col);
		}
		
		System.out.println();
		
		for(Process process : processes) {
			int[] processPrints = { process.getProcessId(), process.getArrivalTime(), process.getBurstTime(), process.getCompletedTime(), process.getTurnaroundTime(), process.getWaitingTime() };
			for(int print : processPrints) {
				System.out.format("%-7s", print);
			}
			System.out.println();
		}
		System.out.println("-------------------------------------");
	}
	

	public void printGanttChart() {
		final int SCALE = 5;
		int totalLength = processes.get(processes.size() -1).getCompletedTime() * SCALE;
		ArrayList<Integer> values = new ArrayList<>();
		
		String lineStr = "==============================================================================================================================================================================================================================================================";
		String idleStr = "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤";
		String line = String.format("%." + totalLength + "s", lineStr);
		
		System.out.println(line);
		
		
		values.add(0);
		int delay = 0;
		for(Process p : processes) {
			int idleTime = 0;
			int length = p.getBurstTime() * SCALE;
			
			StringBuilder strB = new StringBuilder();
			
			// append potential idle time
			if(p.getArrivalTime() > delay) {
				idleTime = (p.getArrivalTime() - delay) * SCALE;
				strB.append("|");
				strB.append(String.format("%." + (idleTime - 2) + "s", idleStr));
				strB.append("|");
				values.add(idleTime / SCALE);
			}
			
			// append Process x centered
			strB.append("|");
			
			int space = length - 2;
			int leftSpace = 1, rightSpace = 1;
			if(space / 2 >= 2) {
				leftSpace = space / 2 + 1;
				rightSpace = space - leftSpace;
			}
			strB.append(String.format("%" + leftSpace + "s", "P" + p.getProcessId()));
			strB.append(String.format("%" + rightSpace + "s", " "));
			
			strB.append("|");
			
			
			System.out.print(strB);
			delay = p.getCompletedTime();
			values.add(p.getBurstTime());
		}
		System.out.println();
		System.out.println(line);

		// Last line with times
		StringBuilder strB2 = new StringBuilder();
		int prevSum = 0;
		for(Integer n : values) {
			int printNr = n + prevSum;
			int printNrLen = (new String() + printNr).length();
			int space = n * SCALE - printNrLen;
			for(int i = 0; i < space; i ++) {
				strB2.append(" ");
			}
			strB2.append(printNr);
			prevSum += n;
		}
		System.out.println(strB2);
	}
		
}
