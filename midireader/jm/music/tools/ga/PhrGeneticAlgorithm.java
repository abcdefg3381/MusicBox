/*
 * PhrGeneticAlgrithm.java 0.1.1 11th December 2000
 *
 * Copyright (C) 2000 Adam Kirby
 *
 * <This Java Class is part of the jMusic API version 1.5, March 2004.>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package jm.music.tools.ga;

import jm.music.data.Phrase;

/**
 * @author Adam Kirby
 * @version 0.1.1, 11th December 2000
 */
public class PhrGeneticAlgorithm {
	private static int rand(final int left, final int right) {
		return left + (int) (Math.random() * (right - left)) + 1;
	}

	protected int beatsPerBar;

	protected boolean finished;

	protected double[] fitness;

	protected FitnessEvaluater fitnessEvaluater;

	protected double initialLength;

	protected int initialSize, originalSize;

	protected long iteration;

	protected Mutater mutater;

	protected ParentSelector parentSelector;

	protected Phrase[] population;

	protected PopulationInitialiser populationInitialiser;

	protected Recombiner recombiner;

	protected SurvivorSelector survivorSelector;

	protected TerminationCriteria terminationCriteria;

	public PhrGeneticAlgorithm(Phrase phrase, int beatsPerBar, PopulationInitialiser populationInitialiser,
			FitnessEvaluater fitnessEvaluater, TerminationCriteria terminationCriteria, ParentSelector parentSelector,
			Recombiner recombiner, Mutater mutater, SurvivorSelector survivorSelector) {

		this.beatsPerBar = beatsPerBar;
		initialLength = phrase.getEndTime();
		initialSize = phrase.size();
		originalSize = initialSize;

		this.populationInitialiser = populationInitialiser;
		this.fitnessEvaluater = fitnessEvaluater;
		this.terminationCriteria = terminationCriteria;
		this.parentSelector = parentSelector;
		this.recombiner = recombiner;
		this.mutater = mutater;
		this.survivorSelector = survivorSelector;

		setUpNewPopulation(phrase);

	}

	public double getAverageFitness() {
		double fitnessCount = 0.0;
		for (double fitnes : fitness) {
			fitnessCount += fitnes;
		}
		return fitnessCount / fitness.length;
	}

	public double getBestFitness() {
		double bestFitness = 0;
		int index = -1;
		for (int i = 0; i < fitness.length; i++) {
			if (fitness[i] > bestFitness) {
				bestFitness = fitness[i];
				index = i;
			}
		}
		return bestFitness;
	}

	public Phrase getBestIndividual() {
		double bestFitness = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < fitness.length; i++) {
			if (fitness[i] < bestFitness) {
				bestFitness = fitness[i];
				index = i;
			}
		}
		return population[index];
	}

	public double[] getFitness() {
		return fitness;
	}

	public long getIteration() {
		return iteration;
	}

	public Mutater getMutater() {
		return mutater;
	}

	/**
	 * Returns the population ordered by fitness with better fitness' first.
	 */
	public Phrase[] getOrderedPopulation() {
		quicksort();
		return population;
	}

	public Phrase[] getPopulation() {
		return population;
	}

	public double getStandardDeviation() {
		double avgFitness = getAverageFitness();
		double stDevCount = 0.0;
		for (double fitnes : fitness) {
			stDevCount += (avgFitness - fitnes) * (avgFitness - fitnes);
		}
		return Math.sqrt(stDevCount) / fitness.length;
	}

	/**
	 * Evolve the population through one generation.
	 */
	public boolean iterate() {
		finished = terminationCriteria.isFinished(population);
		if (!finished) {
			iteration++;

			Phrase[] parents = parentSelector.selectParents(population, fitness);
			Phrase[] recombined = recombiner.recombine(parents, fitness, initialLength, initialSize, beatsPerBar);
			Phrase[] children = mutater.mutate(recombined, initialLength, initialSize, beatsPerBar);

			double[] parentsFitness = fitnessEvaluater.evaluate(children);
			population = survivorSelector.selectSurvivors(population, fitness, children, parentsFitness);
			fitness = fitnessEvaluater.evaluate(population);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Evolve the population through the specified number of generations.
	 */
	public long iterate(long iterations) {

		long iterationsProcessed = iterations;
		for (int i = 0; i < iterations; i++) {
			iterate();
			if (finished) {
				iterationsProcessed = i;
				break;
			}
		}
		return iterationsProcessed;
	}

	private void quicksort() {
		quicksort(0, population.length - 1);
	}

	private void quicksort(final int left, final int right) {
		int last;

		if (left >= right) {
			return;
		}
		swap(left, rand(left, right));
		last = left;
		for (int i = left + 1; i <= right; i++) {
			if (fitness[i] < fitness[left]) {
				swap(++last, i);
			}
		}
		swap(left, last);
		quicksort(left, last - 1);
		quicksort(last + 1, right);
	}

	public void restoreInitialSize() {
		this.initialSize = this.originalSize;
	}

	public void setBeatsPerBar(int beats) {
		this.beatsPerBar = beats;
	}

	public void setUpNewPopulation(Phrase phrase) {
		iteration = 0;
		population = populationInitialiser.initPopulation(phrase, beatsPerBar);
		fitness = fitnessEvaluater.evaluate(population);
	}

	private void swap(final int i, final int j) {
		Phrase tempPhrase = population[i];
		population[i] = population[j];
		population[j] = tempPhrase;

		double tempDouble = fitness[i];
		fitness[i] = fitness[j];
		fitness[j] = tempDouble;
	}

	public void zeroInitialSize() {
		this.originalSize = this.initialSize;
		this.initialSize = 0;
	}
}
