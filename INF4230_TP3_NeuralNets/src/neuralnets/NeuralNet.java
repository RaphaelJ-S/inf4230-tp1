package neuralnets;

import helpers.LabelledRecord;
import helpers.pmml.PMMLReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class NeuralNet {

    public double learningRate;
    public int nbrOutputNeurons;
    public int nbrHiddenLayers;
    public int nbrNeuronsByLayer;
    public int nbrInputNeurons;

    public Neuron[] outputLayer;
    public  Neuron[] inputLayer;
    public Neuron[][] layers;

    public PrintStream output = System.out;//default
    public boolean printComputations = false;

    
    public void configure_hyperparameters(final double _learningRate){
        // We only have learningRate as hyperparameter
        this.learningRate = _learningRate;
    }

    public void train(final int _maxEpoch, final List<LabelledRecord> _sortedRecords){
        /*
        This method trains a multi-layer neural network using stochastic gradient descent (i.e. each data record updates
        the network weights).
         */
        int epoch = 0;
        while(epoch < _maxEpoch) {
            //pour chaque ligne
            for (int i = 0; i != _sortedRecords.size(); ++i) {
                final LabelledRecord currentRecord = _sortedRecords.get(i);
                final int expected = currentRecord.label - 1;
                ForwardPropagation.compute_forward_propagation(this, currentRecord);
                final List<double[]> neuron_deltas = BackPropagation.compute_error_backpropagation(this, expected);
                BackPropagation.update_weights(this, neuron_deltas);
            }
            if(epoch % 50 == 0) this.output.println("epoch "+epoch+" finished");
            epoch += 1;
        }
    }

    public static int identify_output_label(final Neuron[] _outputLayer){
        int argmax = _outputLayer.length;
        double max = Double.MIN_VALUE;
        // on regarde les neuronnes de sortie et on prend argmax
        for(int j=0;j!=_outputLayer.length;++j) {
            if (_outputLayer[j].output > max) {
                max = _outputLayer[j].output;
                argmax = j;
            }
        }
        return argmax;
    }

    public void test(final NeuralNet _network, final List<LabelledRecord> _sortedRecords){
        int mistakes = 0;
        for (int i = 0; i != _sortedRecords.size(); ++i) {
            final LabelledRecord currentRecord = _sortedRecords.get(i);
            final int expected = currentRecord.label - 1;
            final Neuron[] output = ForwardPropagation.compute_forward_propagation(_network, currentRecord);

            int argmax = identify_output_label(output);
            if(argmax != expected){
                mistakes += 1;
            }
        }
        _network.output.println("finished with "+mistakes+"/"+_sortedRecords.size());
    }
	
	public void initialize_network(final PMMLReader.NeuralNetConfiguration _config){
        final Random random = new Random(new Date().getTime());
        final double multiplier = 1;

        this.nbrHiddenLayers = _config.nbrHiddenLayers;
        this.nbrNeuronsByLayer = _config.nbrNeuronsPerHiddenLayer;
        this.nbrOutputNeurons = _config.nbrOutputNeurons;
        this.nbrInputNeurons = _config.nbrInputNeurons;

        // creates an array to store all hidden layers, including input and output
        this.layers = new Neuron[_config.nbrHiddenLayers + 2][];
        // initializes hidden layers
        for(int i=0;i != _config.nbrHiddenLayers;++i){
            this.layers[i+1] = new Neuron[_config.nbrNeuronsPerHiddenLayer];
            Neuron[] current_layer = this.layers[i+1];
            for(int j=0;j!=_config.nbrNeuronsPerHiddenLayer;++j){
                current_layer[j] = new Neuron(i + 1, j);// creates new neuron with its coordinates in the architecture

                if(_config.weights.containsKey(i+1) && _config.weights.get(i+1).containsKey(j)){
                    // ici charge les poids depuis le fichier PMML
                    current_layer[j].initialize_weights_and_bias(
                            _config.weights.get(i+1).get(j).get(i),
                            _config.biases.get(i+1).get(j));
                }
                else{
                    current_layer[j].initialize_weights_and_bias(
                            i == 0 ? _config.nbrInputNeurons : _config.nbrNeuronsPerHiddenLayer, random, multiplier);
                }
            }
        }
        // initializes output layer
        final int output_layer = _config.nbrHiddenLayers + 1;
        this.layers[output_layer] = new Neuron[_config.nbrOutputNeurons];
        this.outputLayer = this.layers[output_layer];
        for(int j=0;j!=_config.nbrOutputNeurons;++j){
            this.outputLayer[j] = new Neuron(output_layer, j);// creates new neuron with its coordinates in the architecture
            if(_config.weights.containsKey(output_layer) && _config.weights.get(output_layer).containsKey(j)){
                // ici charge les poids
                this.outputLayer[j].initialize_weights_and_bias(
                        _config.weights.get(output_layer).get(j).get(output_layer - 1),
                        _config.biases.get(output_layer).get(j));
            }
            else {
                this.outputLayer[j].initialize_weights_and_bias(_config.nbrNeuronsPerHiddenLayer, random, multiplier);
            }
        }
        // initializes input layer
        this.layers[0] = new Neuron[_config.nbrInputNeurons];
        this.inputLayer = this.layers[0];
        for(int j=0;j!=_config.nbrInputNeurons;++j){
            this.inputLayer[j] = new Neuron(0, j);// creates new neuron with its coordinates in the architecture
            if(_config.biases.containsKey(0) && _config.biases.get(0).containsKey(j)) {
                // ici charge les poids
                this.inputLayer[j].initialize_weights_and_bias(null, _config.biases.get(0).get(j));
            }
            else{
                this.inputLayer[j].initialize_weights_and_bias(0, random, multiplier);
            }
        }
    }
}
