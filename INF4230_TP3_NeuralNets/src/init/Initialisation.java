package init;

import helpers.DatasetReader;
import helpers.LabelledRecord;
import helpers.Normalization;
import helpers.pmml.PMMLReader;
import neuralnets.NeuralNet;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Initialisation {
    public static double learningRate = 0d;// -l ou --lrate 0.3
    public static int maxEpoch = 10;// -e ou --epoch 500
    public static double ratioTrainingTest = 0.7d;// -r ou --ratio
    public static PMMLReader.NeuralNetConfiguration config;// -a ou --architecture 4x3x4x1
    public static byte inputDataset;// -d ou --dataset iris | seeds

    public static NeuralNet demarrer_apprentissage_et_tests(final String[] _parametres,
                                                            final PrintStream _output, final boolean _print){
        config = new PMMLReader.NeuralNetConfiguration();
        get_arguments(_parametres, config, _output);

        if(inputDataset == 0){
            _output.println("Unknown dataset. Should be iris or seeds.");
            System.exit(1);
        }
        if(config.nbrInputNeurons <= 0 || config.nbrOutputNeurons <= 0){
            _output.println("Cannot build a neural net with no or negative number of input/output neurons...");
            System.exit(1);
        }
        if(learningRate <= 0){
            _output.println("Cannot build a neural net with a zero or negative learning rate...");
            System.exit(1);
        }
        if(maxEpoch <= 0){
            _output.println("Cannot train a neural net with a zero or negative maximal number of epochs");
            System.exit(1);
        }

        if(config.loadWeightsLocation != null){
            PMMLReader.read(config.loadWeightsLocation, config);
        }

        /*final ArrayList<helpers.LabelledRecord> records = helpers.DatasetReader.load_records(
                "./datasets/seeds_dataset.txt", "\t+", seeds_mapping);*/

        final ArrayList<LabelledRecord> records = inputDataset == IRIS_DATASET ?
                // loads iris dataset
                DatasetReader.load_records("./datasets/iris.data", ",", iris_mapping)
                // or loads seeds dataset instead
                : DatasetReader.load_records("./datasets/seeds_dataset.txt", "\t+", seeds_mapping);

        Normalization.normalize_zero_one(records, config.nbrInputNeurons);

        final DatasetReader.RandomSampling r = new DatasetReader.RandomSampling();
        // faire le split train/test
        final List<LabelledRecord> trainingSet = DatasetReader.build_sample_set(records, ratioTrainingTest, r);

        _output.println("Starting a neural net with architecture: "+config.nbrInputNeurons+"x"+config.nbrOutputNeurons
                +"x"+config.nbrNeuronsPerHiddenLayer+"x"+config.nbrHiddenLayers);
        _output.println("Will stop at epoch "+maxEpoch+", using learning rate="+learningRate);
        _output.println("With ratio training/test of "+ratioTrainingTest+" ("+trainingSet.size()
                +" examples on "+records.size()+" for training).");
        final NeuralNet nn = new NeuralNet();
        nn.printComputations = _print;
        nn.output = _output;//defines where to output logs (i.e. console, textarea)
        nn.initialize_network(config);
        nn.configure_hyperparameters(learningRate);

        nn.train(maxEpoch, trainingSet);
        _output.println("Training finished, starting testing...");
        nn.test(nn, DatasetReader.derive_test_set(r, records));

        //PMMLWriter.export_weights(nn, "./iris_model.pmml");
        return nn;
    }

    public static void get_arguments(final String[] _args, final PMMLReader.NeuralNetConfiguration _config,
                                     final PrintStream _output) {
        final Map<String, String> arguments = new HashMap<>();
        for(int i=0;i<_args.length;i+=2){
            final String argname = _args[i].trim();
            final String argval = _args[i+1].trim();
            arguments.put(argname, argval);
        }

        for (final Map.Entry<String, String> entry : arguments.entrySet()) {
            if (entry.getKey().equals("-l") || entry.getKey().equals("--lrate")) {
                learningRate = Double.parseDouble(entry.getValue());
            } else if (entry.getKey().equals("-a") || entry.getKey().equals("--architecture")) {
                final String[] architecture = entry.getValue().trim().toLowerCase().split("x");
                if (architecture.length == 4) {
                    _config.nbrInputNeurons = Integer.parseInt(architecture[0]);
                    _config.nbrOutputNeurons = Integer.parseInt(architecture[1]);
                    _config.nbrNeuronsPerHiddenLayer = Integer.parseInt(architecture[2]);
                    _config.nbrHiddenLayers = Integer.parseInt(architecture[3]);
                } else {
                    _output.println("Could not parse architecture: " + entry.getValue().trim());
                    _output.println("Should be a [#input]x[#output]x[#neurons per hidden layer]x[#hidden layers]");
                    _output.println("Ex: 4x3x5x1");
                }
            } else if (entry.getKey().equals("-e") || entry.getKey().equals("--epoch")) {
                maxEpoch = Integer.parseInt(entry.getValue().trim());
            } else if (entry.getKey().equals("-r") || entry.getKey().equals("--ratio")) {
                ratioTrainingTest = Double.parseDouble(entry.getValue().trim());
            } else if (entry.getKey().equals("-d") || entry.getKey().equals("--dataset")) {
                // iris ou seeds
                if(entry.getValue().trim().toLowerCase().equals("iris")){
                    inputDataset = IRIS_DATASET;
                }
                else if(entry.getValue().trim().toLowerCase().equals("seeds")){
                    inputDataset = SEEDS_DATASET;
                }
            }
            else if (entry.getKey().equals("-w") || entry.getKey().equals("--weights")) {
                if(entry.getValue().trim().length() != 0) {
                    _config.loadWeightsLocation = entry.getValue().trim();
                }
            }

        }
    }

    public static final byte IRIS_DATASET = 0x01;
    public static final byte SEEDS_DATASET = 0x02;

    final static DatasetReader.ClassLabelMapping seeds_mapping = _x -> Integer.parseInt(_x.trim());
    final static DatasetReader.ClassLabelMapping iris_mapping = _x -> {
        switch(_x){
            case "Iris-setosa":
                return 1;
            case "Iris-versicolor":
                return 2;
            case "Iris-virginica":
                return 3;
            default:
                break;
        }
        return 0;//This should not happen
    };
}
