package com.dimitri.ebrnn.neural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class IO {

	private final String filePath;
	private double[][][][] weights;

	public IO(String filePath) { this.filePath = filePath; }

	public void Read() throws FileNotFoundException {
		File file = new File(filePath);
		weights = count(file);
		Scanner scanner = new Scanner(file);

		int layer = 1;
		int neuron = 0;
		int gate = 0;
		int weight = 0;

		String current;
		scanner.next(); //skip first layer
		// skip whole first layer
		while (scanner.hasNext()) {
			current = scanner.next();
			if (current.equals("Layer:")) {
				break;
			}
		}
		scanner.next(); //skip first neuron
		scanner.next(); //skip first gate

		while (scanner.hasNext()) {
			current = scanner.next();
			switch (current) {
				case "Layer:":
					neuron = 0;
					gate = 0;
					weight = 0;
					layer++;
					scanner.next(); //skip first neuron
					scanner.next(); //skip first gate
					break;
				case "Neuron:":
					gate = 0;
					weight = 0;
					neuron++;
					scanner.next(); //skip first gate
					break;
				case "Gate:":
					weight = 0;
					gate++;
					break;
				default:
					weights[layer][neuron][gate][weight] = Double.parseDouble(current);
					weight++;
					break;
			}
		}

		scanner.close();
	}

	public void Write(Net net) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(filePath);
		double[][][][] weights = net.getWeights();

		if (weights.length == 0) {
			throw new FileNotFoundException("NO WEIGHTS!");
		}

		for (double[][][] weight : weights) {
			printWriter.print("Layer:\n");
			for (double[][] doubles : weight) {
				printWriter.print("\tNeuron:\n");
				for (double[] aDouble : doubles) {
					printWriter.print("\t\tGate:\n");
					for (double v : aDouble) {
						printWriter.print("\t\t\t" + v + "\n");
					}
				}
			}
		}
		printWriter.close();
	}

	public double[][][][] count(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		String current;
		int layers = 0;
		while (scanner.hasNext()) {
			current = scanner.next();
			if (current.equals("Layer:")) {
				layers++;
			}
		}

		double[][][][] weights = new double[layers][][][];

		scanner = new Scanner(file);
		scanner.next(); // skip first layer

		layers = 0;
		int neuron = 0;
		while (scanner.hasNext()) {
			current = scanner.next();
			if (current.equals("Layer:")) {
				weights[layers] = new double[neuron][][];
				layers++;
				neuron = 0;
			} else if (current.equals("Neuron:")) {
				neuron++;
			}
		}
		weights[layers] = new double[neuron][][];

		scanner = new Scanner(file);
		scanner.next(); // skip first layer
		scanner.next(); // skip first neuron

		layers = 0;
		neuron = 0;
		int gate = 0;
		while (scanner.hasNext()) {
			current = scanner.next();
			switch (current) {
				case "Layer:":
					weights[layers][neuron] = new double[gate][];
					layers++;
					neuron = 0;
					gate = 0;
					scanner.next(); // skip first neuron
					break;
				case "Neuron:":
					weights[layers][neuron] = new double[gate][];
					neuron++;
					gate = 0;
					break;
				case "Gate:":
					gate++;
					break;
			}
		}

		weights[layers][neuron] = new double[gate][];

		scanner = new Scanner(file);
		scanner.next(); // skip first layer
		// skip whole first layer
		while (scanner.hasNext()) {
			current = scanner.next();
			if (current.equals("Layer:")) {
				break;
			}
		}
		scanner.next(); // skip first neuron
		scanner.next(); // skip first gate

		layers = 1;
		neuron = 0;
		gate = 0;
		int weight = 0;
		while (scanner.hasNext()) {
			current = scanner.next();
			switch (current) {
				case "Layer:":
					weights[layers][neuron][gate] = new double[weight];
					layers++;
					neuron = 0;
					gate = 0;
					weight = 0;
					scanner.next(); // skip first neuron
					scanner.next(); // skip first gate
					break;
				case "Neuron:":
					weights[layers][neuron][gate] = new double[weight];
					neuron++;
					gate = 0;
					weight = 0;
					scanner.next(); // skip first gate
					break;
				case "Gate:":
					weights[layers][neuron][gate] = new double[weight];
					gate++;
					weight = 0;
					break;
				default:
					weight++;
					break;
			}
		}

		weights[layers][neuron][gate] = new double[weight];

		return weights;
	}

	public double[][][] getLayerWeights(int layerIndex) {
		return weights[layerIndex];
	}

	public double[][][][] getWeights() {
		return weights;
	}

	public int[] getTopology() throws IOException {
		if (!weights.equals(null)) {
			int[] topology = new int[weights.length];

			for (int i = 0; i < weights.length; i++) {
				topology[i] = weights[i].length;
			}

			return topology;
		} else {
			throw new IOException("No topology has been derived yet");
		}
	}

}
