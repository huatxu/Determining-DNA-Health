package dnaHealth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Driver {
	public static void main(String... args) throws IOException {
		Path inputFolder = Paths.get(".\\input");
		Files.list(inputFolder).forEach(e -> drive(e));
		}

	private static void drive(Path path) {
		int[] results = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
			int nGenes = Integer.parseInt(reader.readLine());
			String[] genes = reader.readLine().split(" ");
			int[] genesScores = Stream.of(reader.readLine().split(" ")).mapToInt(e -> Integer.parseInt(e)).toArray();
			int nStrands = Integer.parseInt(reader.readLine());
			DNA[] strands = new DNA[nStrands];
			for(int i = 0; i < nStrands; i++) {
				strands[i] = new DNA(reader.readLine());
			}
			results = process(strands, genes, genesScores);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try(PrintWriter writer = new PrintWriter(new FileWriter(new File(path.toString().replace("input", "output"))))) {
			writer.print(results[0] + " " + results[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int[] process(DNA[] strands, String[] genes, int[] genesScores) {
		int minQ = Integer.MAX_VALUE;
		int maxQ = Integer.MIN_VALUE;
		for (int i = 0; i < strands.length; i++) {
			DNA next = strands[i];
			String seq = next.seq;
			int currentScore = 0;
			for(int j = next.beneficialGenes[0]; j < next.beneficialGenes[1] + 1; j++) {
				String testingGene = genes[j];
				int geneScore = genesScores[j];
				for (int k = 0; k < seq.length(); k++) {
					if(k + testingGene.length() <= seq.length() && seq.substring(k, testingGene.length() + k).equals(testingGene)) {
						currentScore += geneScore;
					}
				}
			}
			if(currentScore < minQ)
				minQ = currentScore;
			if(currentScore > maxQ)
				maxQ = currentScore;
		}
		int[] result = {minQ, maxQ};
		return result;
	}

	private static class DNA {
		int[] beneficialGenes = new int[2];
		String seq;
		private DNA (String line) {
			beneficialGenes[0] = Integer.parseInt(line.split(" ")[0]);
			beneficialGenes[1] = Integer.parseInt(line.split(" ")[1]);
			seq = line.split(" ")[2];
		}
	}
}
