package mainApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EvolutionViewer {

	static final int FRAME_WIDTH = 1800;
	static final int FRAME_HEIGHT = 600;
	static final Color LIGHT_GRAY = new Color(200, 200, 200);

	private int genomeLengthVal, populationVal, numGenerationsVal, mutationRateVal, elitismNum, clicked, fitClick;
	private String selectionType, evolveType;

	// How long to wait in milliseconds between each step of the simulation
	private static final int DELAY = 50;
	Timer t;

	public EvolutionViewer() {
		// Creates frames
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		// Creates instance of EvolutionComponent
		EvolutionComponent newComponent = new EvolutionComponent(new EvolutionLoop(populationVal, this.genomeLengthVal),
				populationVal);

		// Adding buttons and creating layout
		GridBagLayout grid = new GridBagLayout();

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(grid);

		JTextField mutationRate = new JTextField("Enter Mutation Rate", 0);
		JTextField numGenerations = new JTextField("Enter Number of Generations", 0);
		JTextField population = new JTextField("Enter Population", 0);
		JTextField genomeLength = new JTextField("Enter Genome Length", 0);

		JButton enterButton = new JButton("Start");
		JTextField elitismNumButton = new JTextField("Number of Elites", 0);
		JCheckBox terminateAtMaxButton = new JCheckBox("Terminate at Max Fitness?");
		JCheckBox crossoverOption = new JCheckBox("Crossover?");

		JButton seeFitChrom = new JButton("Show Fittest Chromosome");

		String[] selectionChoices = { "Truncation", "Roulette", "Rank" };

		final JComboBox<String> cb = new JComboBox<String>(selectionChoices);

		String[] evolveChoices = { "Regular", "Elitism" };

		final JComboBox<String> cb2 = new JComboBox<String>(evolveChoices);

		// Add things to frame
		inputPanel.add(enterButton);
		inputPanel.add(genomeLength);
		inputPanel.add(population);
		inputPanel.add(numGenerations);
		inputPanel.add(mutationRate);
		inputPanel.add(elitismNumButton);
		inputPanel.add(cb);
		inputPanel.add(cb2);
		inputPanel.add(terminateAtMaxButton);
		inputPanel.add(crossoverOption);
		inputPanel.add(seeFitChrom);

		frame.add(inputPanel, BorderLayout.SOUTH);
		frame.add(newComponent, BorderLayout.CENTER);

		JButton clear = new JButton("Clear All");

		// Add button panel on the right side

		// Starts the simulator
		Timer t = new Timer(DELAY, new ActionListener() {
			public int ticks = 0;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ticks == numGenerationsVal - 1) {
					return;
				}

				if (terminateAtMaxButton.isSelected()) {
					if (newComponent.highFit.get(ticks) == (Integer) genomeLengthVal) {
						return;
					}

				}

				frame.repaint();
				newComponent.repaint();
				// new ChromosomeViewer(newComponent.fittest.get(ticks));
				ticks++;

			}

		});

		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clicked++;
				if (clicked == 1) {
					enterButton.setText("Pause");

					genomeLengthVal = Integer.parseInt(genomeLength.getText());
					numGenerationsVal = Integer.parseInt(numGenerations.getText());
					populationVal = Integer.parseInt(population.getText());
					mutationRateVal = Integer.parseInt(mutationRate.getText());
					selectionType = cb.getSelectedItem().toString();
					evolveType = cb2.getSelectedItem().toString();
					elitismNum = Integer.parseInt(elitismNumButton.getText());
					newComponent.genSize = numGenerationsVal;
					newComponent.startUp(populationVal, genomeLengthVal);

					if (selectionType.equals("Truncation")) {
						if (evolveType.equals("Regular")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runTruncation(crossoverOption.isSelected(), mutationRateVal);
							}

						}
						if (evolveType.equals("Elitism")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runTruncationElite(crossoverOption.isSelected(), mutationRateVal,
										elitismNum);
							}
						}
					}

					if (selectionType.equals("Roulette")) {
						if (evolveType.equals("Regular")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runRoulette(crossoverOption.isSelected(), mutationRateVal);
							}

						}
						if (evolveType.equals("Elitism")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runRouletteElite(crossoverOption.isSelected(), mutationRateVal,
										elitismNum);
							}
						}
					}

					if (selectionType.equals("Rank")) {
						if (evolveType.equals("Regular")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runRank(crossoverOption.isSelected(), mutationRateVal);
							}
						}

						if (evolveType.equals("Elitism")) {
							for (int i = 0; i < numGenerationsVal; i++) {
								newComponent.runRankElite(crossoverOption.isSelected(), mutationRateVal, elitismNum);
							}
						}
					}

					t.start();
					clicked++;
				}

				else if (clicked % 2 == 1) {
					t.stop();
					enterButton.setText("Start");
				} else {
					t.start();
					enterButton.setText("Pause");
				}

			}

		});

		seeFitChrom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ChromosomeViewer(newComponent.fittest.get(fitClick));

			}

		});

		// Creates timer and runs simulation

		frame.pack();
		frame.setVisible(true);

	}

}
