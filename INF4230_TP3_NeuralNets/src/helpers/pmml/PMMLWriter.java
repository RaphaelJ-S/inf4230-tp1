package helpers.pmml;

import neuralnets.NeuralNet;
import neuralnets.Neuron;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class PMMLWriter {

    public final static java.nio.charset.Charset ENCODAGE = Charset.forName("UTF-8");
    public static void export_weights(final NeuralNet _network, final String _file_name){
        final String neural_inputs_template = "     <NeuralInputs numberOfInputs=\"%s\">\n%s\n     </NeuralInputs>";
        final String neural_input_template = "      <NeuralInput id=\"%s,%s\" bias=\"%s\">\n" +
                "        <DerivedField optype=\"continuous\" dataType=\"double\">\n" +
                "          <FieldRef field=\"%s\"/>\n" +
                "        </DerivedField>\n" +
                "      </NeuralInput>";
        final String neural_layer_template = "     <NeuralLayer>\n%s\n     </NeuralLayer>";
        final String neural_connection_template = "         <Con from=\"%s,%s\" weight=\"%s\"/>";
        final String neuron_template = "      <Neuron id=\"%s,%s\" bias=\"%s\">\n%s\n      </Neuron>";
        final String neural_outputs_template = "     <NeuralOutputs numberOfOutputs=\"%s\">\n%s\n     </NeuralOutputs>";
        final String neural_output_template = "      <NeuralOutput outputNeuron=\"%s,%s\">\n" +
                "        <DerivedField optype=\"categorical\" dataType=\"string\">\n" +
                "          <NormDiscrete field=\"class\" value=\"%s\"/>\n" +
                "        </DerivedField>\n" +
                "      </NeuralOutput>";
        final String neural_network_template = "    <NeuralNetwork functionName=\"%s\" algorithmName=\"%s\" " +
                "activationFunction=\"%s\" normalizationMethod=\"%s\" numberOfLayers=\"%s\">\n%s\n  </NeuralNetwork>";
        final String pmml_format_template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PMML version=\"4.1\" xmlns=\"http://www.dmg.org/PMML-4_1\">\n" +
                "  <Header copyright=\"UQAM, INF-4230\">\n" +
                "    <Application name=\"INF-4230 TP3\" version=\"0.0.1\"/>\n" +
                "  </Header>\n%s\n</PMML>";
        final String function_name = "classification";
        final String algorithm_name = "RProp";
        final String activation_function = "logistic";
        final String normalization_method = "none";

        final String number_layers = ""+(_network.layers.length - 1);


        String neural_layers_string = null;
        {
            String[] neural_layers = new String[(_network.layers.length - 1)];
            for (int i = 1; i != _network.layers.length; ++i) {
                String[] neural_layer = new String[_network.layers[i].length];
                for (int j = 0; j != _network.layers[i].length; ++j) {
                    final Neuron n =_network.layers[i][j];
                    String[] neural_weights = new String[n.weights.length];
                    for (int w = 0; w != n.weights.length; ++w) {
                        neural_weights[w] = String.format(neural_connection_template, i - 1, w, n.weights[w]);
                    }
                    neural_layer[j] = String.format(neuron_template, n.layer, n.pos, n.bias, String.join("\n", neural_weights));
                }
                neural_layers[i - 1] = String.format(neural_layer_template, String.join("\n", neural_layer));
            }
            neural_layers_string = String.join("\n", neural_layers);
        }


        String neural_inputs_string = null;
        {
            String[] neural_inputs = new String[_network.inputLayer.length];
            for (int j = 0; j != _network.inputLayer.length; ++j) {
                neural_inputs[j] = String.format(neural_input_template, 0, _network.inputLayer[j].pos,
                        _network.inputLayer[j].bias, "field_"+j);
            }
            neural_inputs_string = String.format(neural_inputs_template, _network.inputLayer.length,
                    String.join("\n", neural_inputs));
        }

        String neural_outputs_string = null;
        {
            String[] neural_outputs = new String[_network.outputLayer.length];
            for (int j = 0; j != _network.outputLayer.length; ++j) {
                neural_outputs[j] = String.format(neural_output_template, _network.outputLayer[j].layer,
                        _network.outputLayer[j].pos, "class_"+j);
            }
            neural_outputs_string = String.format(neural_outputs_template, _network.outputLayer.length,
                    String.join("\n", neural_outputs));
        }

        final String file_contents = String.format(pmml_format_template,
                String.format(neural_network_template, function_name, algorithm_name,
                        activation_function, normalization_method, number_layers,
                        neural_inputs_string + "\n" + neural_layers_string + "\n" + neural_outputs_string) );

        //System.out.println(file_contents);

        final Path p = Paths.get(_file_name);
        try(final OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, TRUNCATE_EXISTING))){
            out.write(file_contents.getBytes(ENCODAGE));
            out.flush();
        }
        catch(final IOException e){
            System.out.println("could not read queries file... " + e);
        }
    }
}
