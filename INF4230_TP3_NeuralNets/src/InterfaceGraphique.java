import helpers.pmml.PMMLWriter;
import neuralnets.NeuralNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import static init.Initialisation.demarrer_apprentissage_et_tests;

public class InterfaceGraphique extends JFrame {
    final JTextArea outputArea = new JTextArea();
    final JTextField learningRate = new JTextField();
    final JTextField nombreCouches = new JTextField();
    final JTextField nombreNeuronnes = new JTextField();
    final JTextField maxEpoch = new JTextField();
    final JComboBox datasetSelector = new JComboBox();
    final JCheckBox loadPretrainedWeights = new JCheckBox();
    final JTextField ratioTraniningTest = new JTextField();
    final JCheckBox printComputations = new JCheckBox();

    public static NeuralNet currentNeuralNet = null;
    public static String currentDataset = null;
    public static String currentArch = null;

    public static void main(final String[] _args){
        load_gui();
    }

    public static void load_gui(){
        InterfaceGraphique gui = new InterfaceGraphique();
    }

    public InterfaceGraphique(){
        this.setTitle("INF4230 - TP3");

        final JButton startButton = new JButton();
        startButton.setText("START");
        startButton.setToolTipText("Demarrer l'entraintenent et test");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final PrintStream printStream = new PrintStream(new CustomOutputStream(outputArea));
                        printStream.println("---------------------------------");

                        String architecture = "";
                        final String dataset = datasetSelector.getSelectedItem().toString();
                        if("iris".equals(dataset)){
                            architecture = "4x3";
                        }
                        else if("seeds".equals(dataset)){
                            architecture = "7x3";
                        }
                        else{
                            printStream.println("Wrong dataset... Aborting.");
                            return;
                        }
                        final String nbrNeuronnes = nombreNeuronnes.getText();
                        if(nbrNeuronnes != null  && nbrNeuronnes.trim().length() != 0){
                            architecture += "x"+nbrNeuronnes;
                        }
                        else architecture += "x1";

                        final String nbrCouches = nombreCouches.getText();
                        if(nbrCouches != null && nbrCouches.trim().length() != 0){
                            architecture += "x"+nbrCouches;
                        }
                        else architecture += "x1";

                        String ratio = ratioTraniningTest.getText();
                        if(ratio == null || ratio.trim().length() == 0){
                            ratio = "0.6";
                        }

                        String epoch = maxEpoch.getText();
                        if(epoch == null || epoch.trim().length() == 0){
                            epoch = "500";
                        }

                        String lrate = learningRate.getText();
                        if(lrate == null || lrate.trim().length() == 0){
                            lrate = "0.5";
                        }

                        //PMMLReader.read("./iris_model.pmml", config);
                        currentArch = architecture;
                        currentDataset = datasetSelector.getSelectedItem().toString();
                        final String[] args = new String[]{
                                "--lrate", lrate,
                                "--epoch", epoch,
                                "--ratio", ratio,
                                "-a", architecture,
                                "-d", datasetSelector.getSelectedItem().toString(),
                                "-w", loadPretrainedWeights.isSelected()
                                && currentDataset.equals("iris") ? "./"+currentDataset+"_model.pmml" : ""
                        };
                        currentNeuralNet = demarrer_apprentissage_et_tests(args, printStream, printComputations.isSelected());
                        printStream.println("---------------------------------");
                    }
                }).start();
            }
        });

        final JButton exportWeightsButton = new JButton();
        exportWeightsButton.setText("EXPORT");
        exportWeightsButton.setToolTipText("Exporter les poids");
        exportWeightsButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent evt) {
                if(currentNeuralNet != null && currentDataset != null && currentArch != null){
                    PMMLWriter.export_weights(currentNeuralNet,
                            "./pretrained/"+currentDataset+"_model_"+currentArch+"_"+(new Date().getTime())+".pmml");
                }
            }
        });


        datasetSelector.addItem("iris");
        datasetSelector.addItem("seeds");
        datasetSelector.setToolTipText("Selectionner le jeu de données à utiliser dans la liste");

        final JPanel champsHyperparametres = new JPanel();
        champsHyperparametres.setLayout(new BoxLayout(champsHyperparametres, BoxLayout.Y_AXIS));

        //final JTextField learningRate = new JTextField();
        learningRate.setText("0.3");
        learningRate.setColumns(10);
        learningRate.setToolTipText("Entrer le taux d'apprentissage ou learning rate");

        //final JTextField nombreCouches = new JTextField();
        nombreCouches.setColumns(10);
        nombreCouches.setText("1");
        nombreCouches.setToolTipText("Entrer le nombre de couches cachées");

        //final JTextField nombreNeuronnes = new JTextField();
        nombreNeuronnes.setText("7");
        nombreNeuronnes.setColumns(10);
        nombreNeuronnes.setToolTipText("Entrer le nombre de neuronnes par couche cachée");

        //final JTextField maxEpoch = new JTextField();
        maxEpoch.setText("500");
        maxEpoch.setColumns(10);
        maxEpoch.setToolTipText("Saisir le nombre d'iterations maximales pour l'entrainement");

        final JLabel labelLearningRate = new JLabel();
        labelLearningRate.setText("Taux d'apprentissage");
        labelLearningRate.setToolTipText(learningRate.getToolTipText());
        final JLabel labelNombreCouches = new JLabel();
        labelNombreCouches.setText("Nombre de couches cachées");
        labelNombreCouches.setToolTipText(nombreCouches.getToolTipText());
        final JLabel labelNombreNeuronnes = new JLabel();
        labelNombreNeuronnes.setText("Nombre de neuronnes par couche");
        labelNombreNeuronnes.setToolTipText(nombreNeuronnes.getToolTipText());
        final JLabel labelMaxEpoch = new JLabel();
        labelMaxEpoch.setText("Epoque maximale");
        labelMaxEpoch.setToolTipText(maxEpoch.getToolTipText());

        final JPanel panelLearningRate = new JPanel();
        panelLearningRate.setLayout(new BorderLayout());

        panelLearningRate.add(labelLearningRate, BorderLayout.WEST);
        panelLearningRate.add(learningRate, BorderLayout.CENTER);
        champsHyperparametres.add(panelLearningRate);
        labelLearningRate.setLabelFor(learningRate);

        final JPanel panelNombreCouches = new JPanel();
        panelNombreCouches.setLayout(new BorderLayout());
        panelNombreCouches.add(labelNombreCouches, BorderLayout.WEST);
        panelNombreCouches.add(nombreCouches, BorderLayout.CENTER);
        champsHyperparametres.add(panelNombreCouches);


        final JPanel panelNombreNeuronnes = new JPanel();
        panelNombreNeuronnes.setLayout(new BorderLayout());
        panelNombreNeuronnes.add(labelNombreNeuronnes, BorderLayout.WEST);
        panelNombreNeuronnes.add(nombreNeuronnes, BorderLayout.CENTER);
        champsHyperparametres.add(panelNombreNeuronnes);

        final JPanel panelMaxEpoch = new JPanel();
        panelMaxEpoch.setLayout(new BorderLayout());

        panelMaxEpoch.add(labelMaxEpoch, BorderLayout.WEST);
        panelMaxEpoch.add(maxEpoch, BorderLayout.CENTER);

        champsHyperparametres.add(panelMaxEpoch);

        final JPanel optionsDatasets = new JPanel();
        optionsDatasets.setLayout(new BoxLayout(optionsDatasets,  BoxLayout.Y_AXIS));


        final JLabel labelChoixDataset = new JLabel();
        labelChoixDataset.setText("Choisir jeu de données");
        labelChoixDataset.setToolTipText(datasetSelector.getToolTipText());

        final JPanel panelChoixDataset = new JPanel();
        panelChoixDataset.setLayout(new BorderLayout());
        panelChoixDataset.add(labelChoixDataset, BorderLayout.WEST);
        panelChoixDataset.add(datasetSelector, BorderLayout.CENTER);
        labelChoixDataset.setLabelFor(datasetSelector);

        optionsDatasets.add(panelChoixDataset);
        //optionsDatasets.add(datasetSelector);

        final JPanel panelChargerPoids = new JPanel();
        panelChargerPoids.setLayout(new BorderLayout());

        final JLabel labelChargerPoids = new JLabel();
        labelChargerPoids.setText("Utiliser poids pré-entrainés ?");
        loadPretrainedWeights.setToolTipText("Initialiser les poids du réseau avec un modele pre-entrainé.");

        panelChargerPoids.add(labelChargerPoids, BorderLayout.WEST);
        panelChargerPoids.add(loadPretrainedWeights, BorderLayout.CENTER);

        labelChargerPoids.setLabelFor(loadPretrainedWeights);
        labelChargerPoids.setToolTipText(loadPretrainedWeights.getToolTipText());

        optionsDatasets.add(panelChargerPoids);

        final JPanel panelRatio = new JPanel();
        panelRatio.setLayout(new BorderLayout());


        final JLabel labelRatioTrainingTest = new JLabel();
        ratioTraniningTest.setToolTipText("Pourcentage de lignes pour entrainement. Le complement est pour le test.");
        labelRatioTrainingTest.setToolTipText(ratioTraniningTest.getToolTipText());
        labelRatioTrainingTest.setText("Ratio entrainement/test");

        panelRatio.add(labelRatioTrainingTest, BorderLayout.WEST);
        ratioTraniningTest.setText(0.6+"");
        panelRatio.add(ratioTraniningTest, BorderLayout.CENTER);

        optionsDatasets.add(panelRatio);


        final JPanel boutonsStartExport = new JPanel();
        boutonsStartExport.setLayout(new BorderLayout());

        final JPanel boutonsStartOptionsPanel = new JPanel();
        boutonsStartOptionsPanel.setLayout(new BorderLayout());
        boutonsStartOptionsPanel.add(startButton, BorderLayout.WEST);

        final JPanel boutonsPrintComputationsPanel = new JPanel();
        boutonsPrintComputationsPanel.setLayout(new BorderLayout());
        boutonsStartOptionsPanel.add(boutonsPrintComputationsPanel, BorderLayout.CENTER);


        boutonsPrintComputationsPanel.add(printComputations, BorderLayout.CENTER);
        final JLabel labelPrintComputations = new JLabel();
        labelPrintComputations.setText("Afficher les sortie ?");
        labelPrintComputations.setLabelFor(printComputations);
        printComputations.setToolTipText("Afficher ou non les calculs dans la couche de sortie (ATTENTION: deconseillé)");
        labelPrintComputations.setToolTipText(printComputations.getToolTipText());

        boutonsPrintComputationsPanel.add(labelPrintComputations, BorderLayout.WEST);

        boutonsStartExport.add(boutonsStartOptionsPanel, BorderLayout.WEST);

        boutonsStartExport.add(exportWeightsButton, BorderLayout.EAST);

        final JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(outputArea);

        outputArea.setSize(750, 500);
        outputPanel.add(scroll, BorderLayout.CENTER);


        final JPanel rootPanel = new JPanel();

        getContentPane().setLayout(new BorderLayout(100, 100));


        JSplitPane hautBas = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rootPanel, outputPanel);

        getContentPane().add(hautBas, BorderLayout.CENTER);

        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(champsHyperparametres, BorderLayout.WEST);
        rootPanel.add(optionsDatasets, BorderLayout.EAST);
        rootPanel.add(boutonsStartExport, BorderLayout.SOUTH);
        rootPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        rootPanel.setSize(750, 500);
        this.getContentPane().setSize(750, 500);
        this.setSize(750, 500);
        this.setVisible(true);
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
        //this.pack();
        init_dispatcher();
    }


    public static char[] buffer_output_1 = new char[10000];
    public static char[] buffer_output_2 = new char[10000];
    public static int buffer_output_1_sz = 0;
    public static int buffer_output_2_sz = 0;
    public static boolean buff_output_1_busy = false;
    public static boolean buff_output_2_busy = false;

    public static Thread dispatcher;
    public static JTextArea dispatcher_target;

    public static void init_dispatcher(){
        dispatcher = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(!buff_output_2_busy && buffer_output_2_sz != 0){
                        buff_output_2_busy = true;
                        String pending = String.valueOf(buffer_output_2, 0, buffer_output_2_sz);
                        dispatcher_target.append(pending);
                        dispatcher_target.update(dispatcher_target.getGraphics());
                        buffer_output_2_sz = 0;
                        buff_output_2_busy = false;
                    }

                    if(!buff_output_1_busy && buffer_output_1_sz != 0){
                        buff_output_1_busy = true;
                        String pending = String.valueOf(buffer_output_1, 0, buffer_output_1_sz);
                        dispatcher_target.append(pending);
                        dispatcher_target.update(dispatcher_target.getGraphics());
                        buffer_output_1_sz = 0;
                        buff_output_1_busy = false;
                    }
                }

            }
        });
        dispatcher.start();
    }

    public class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
            dispatcher_target = this.textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area

            if(!buff_output_1_busy){
                if(buffer_output_1.length <= buffer_output_1_sz){
                    char[] temp_buffer = new char[buffer_output_1.length + 64000];
                    System.arraycopy(buffer_output_1, 0, temp_buffer, 0, buffer_output_1_sz);
                    buffer_output_1 = temp_buffer;
                }

                buffer_output_1[buffer_output_1_sz++] = (char)b;
            }
            else if(!buff_output_2_busy){
                if(buffer_output_2.length <= buffer_output_2_sz) {
                    char[] temp_buffer = new char[buffer_output_2.length + 64000];
                    System.arraycopy(buffer_output_2, 0, temp_buffer, 0, buffer_output_2_sz);
                    buffer_output_2 = temp_buffer;
                }
                buffer_output_2[buffer_output_2_sz++] = (char)b;
            }
        }
    }
}
