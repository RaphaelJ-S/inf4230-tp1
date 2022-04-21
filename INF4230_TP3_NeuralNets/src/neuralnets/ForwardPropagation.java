package neuralnets;

import helpers.LabelledRecord;

import java.io.PrintStream;
import java.util.Arrays;

import static neuralnets.Neuron.activate;
import static neuralnets.Neuron.aggregate;

public class ForwardPropagation {

    public static Neuron[] compute_forward_propagation(final NeuralNet _network, final LabelledRecord _record){

        final int nbrHiddenNeuronsByLayer = _network.nbrNeuronsByLayer;
        final int nbrHiddenLayers = _network.nbrHiddenLayers;
        final int nbrOutputs = _network.nbrOutputNeurons;

        // first layer
        for(int i=0;i!=_network.inputLayer.length;++i){
            _network.inputLayer[i].output = _record.data[i];//a_i <- x_i
        }

        // hidden layers
        for(int i=1;i!=_network.layers.length-1;++i){
            final Neuron[] current_layer = _network.layers[i];
            final Neuron[] previous_layer = _network.layers[i - 1];
            for(int j=0;j!=nbrHiddenNeuronsByLayer;++j){
                double in_j = aggregate(previous_layer, current_layer[j]);//sum w_ij * a_i
                current_layer[j].output = activate(in_j);//g(in_j)
            }
        }

        // computes outputs for final layer (can be merged with previous loop)
        for(int j=0;j!=nbrOutputs;++j) {
            final Neuron[] previous_layer = _network.layers[_network.layers.length - 2];
            double in_j = aggregate(previous_layer, _network.outputLayer[j]);
            _network.outputLayer[j].output = activate(in_j);
        }
        if(_network.printComputations)
            _network.output.println("(output) layer #"+(1+nbrHiddenLayers)+"="+ Arrays.toString(_network.outputLayer));
        return _network.outputLayer;
    }

}
