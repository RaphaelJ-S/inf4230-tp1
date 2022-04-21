package neuralnets;


import java.util.*;

import static neuralnets.Neuron.derivative;

public class BackPropagation {

    public static List<double[]> compute_error_backpropagation(final NeuralNet _network, final int _expected){
        // List of values by layers representing the error backpropagation (i.e. a double[] for each layer)
		final List<double[]> neuron_deltas = new ArrayList<>();
        // handle the output layer first
        
        // handle all other layers
		
        return neuron_deltas;
    }


    public static void update_weights(final NeuralNet _network, final List<double[]> _neuron_deltas){
        // update all layers using _neuron_deltas
    }

}
