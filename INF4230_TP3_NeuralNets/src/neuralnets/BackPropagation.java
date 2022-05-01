package neuralnets;


import java.util.*;
import static neuralnets.Neuron.derivative;

public class BackPropagation {

    /***
     * Calcule la différence d'erreur pour toutes les neurones du réseau et les retournent en ordre inverse. ie: première liste de double est les erreurs de la sortie, puis la dernière couche cachée etc.
     * 
     * @param _network Le réseau de neurone avec la forward propagation déjà calculée
     * @param _expected La classe attendue des entrées
     * 
     * @return Une liste des différences d'erreurs pour chaque couches du réseau en commençant pas la fin du réseau
     */
    public static List<double[]> compute_error_backpropagation(final NeuralNet _network, final int _expected){
        // List of values by layers representing the error backpropagation (i.e. a double[] for each layer)
		final List<double[]> neuron_deltas = new ArrayList<>();
        // handle the output layer first
        double [] outputLayerDeltas = new double[_network.nbrOutputNeurons];
        int i = 0; // pour l'index dans le tableau de deltas puisque neuron_deltas est une liste d'array
        for(Neuron output : _network.layers[_network.layers.length - 1]) {

            outputLayerDeltas[i++] = (_expected - output.output) * output.output * (1 - output.output);

        }
        neuron_deltas.add(outputLayerDeltas);
        // handle all other layers - Pas besoin de faire les sorties(déjà faites) et les entrées.
        for(int j = _network.layers.length - 2; j >= 1 ; j--){
            double [] hiddenLayerDeltas = new double[_network.layers[j].length];
            for(int k = 0; k < _network.layers[j].length; k++) {
            
                hiddenLayerDeltas[k] = computeCurrentHiddenDelta(_network.layers[j][k], neuron_deltas.get(neuron_deltas.size() - 1), _network.layers[j+1], k);
            }
            neuron_deltas.add(hiddenLayerDeltas);
        }
        
        return neuron_deltas;
    }

    /***
     * Calcule la différence d'erreur pour une neurone d'une couche cachée
     * 
     * @param currentNeuron La neurone pour laquelle on veut calculer la différence d'erreur
     * @param previousDeltas Les différences d'erreurs de la couche précédente
     * @param previousLayer La couche de neurones précédente
     * @param indexCurrentLayer L'index qui représente 'currentNeuron' dans les poids du 'previousLayer'
     * 
     * @return La différence d'erreur de 'currentNeuron'
     */
    private static double computeCurrentHiddenDelta(Neuron currentNeuron, double[] previousDeltas, Neuron[] previousLayer, int indexCurrentLayer) {
        double sum = 0;
        for(int i = 0; i < previousDeltas.length; i++) {
            sum += previousLayer[i].weights[indexCurrentLayer] * previousDeltas[i]; //somme de w_ij * delta_j
        }
        return currentNeuron.output * (1 - currentNeuron.output) * sum; // output_i * (1 - output_i) * somme de w_ij * delta_j
    }

    /***
     * Mets à jours les poids de toutes les neurones du réseau
     * 
     * @param _network Le réseau de neurones à mettre à jours
     * @param _neuron_deltas Les différences d'erreurs du réseau - Est ordonné inversément par rapport au réseau ie: indice 0 est la couche de sortie.
     */
    public static void update_weights(final NeuralNet _network, final List<double[]> _neuron_deltas){
        // update all layers using _neuron_deltas, starting from the output layer
        for(int i = _network.layers.length - 1; i > 0 ; i--) {
            Neuron [] curr = _network.layers[i];
            Neuron [] prev = _network.layers[i-1];
            double [] deltas = _neuron_deltas.isEmpty() ? null : _neuron_deltas.remove(0);
            for(int k = 0; k < curr.length; k++) {
                for(int j = 0; j < curr[k].weights.length; j++) {
                    curr[k].weights[j] += _network.learningRate * prev[j].output * deltas[k];
                }
            }
        }
    }

}
