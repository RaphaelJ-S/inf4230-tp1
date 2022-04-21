package helpers.pmml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PMMLReader {

    public static class NeuralNetConfiguration{
        public int nbrInputNeurons = 0;
        public int nbrOutputNeurons = 0;
        public int nbrHiddenLayers = 0;
        public int nbrNeuronsPerHiddenLayer = 0;
        public String loadWeightsLocation = null;

        public Map<Integer, Map<Integer, Map<Integer, Map<Integer, Double>>>> weights = new HashMap<>();
        public Map<Integer, Map<Integer, Double>> biases = new HashMap<>();
    }

    public static void read(final String _file_name, final NeuralNetConfiguration _config){
        _config.nbrHiddenLayers = 0;
        _config.nbrNeuronsPerHiddenLayer = 0;
        _config.nbrOutputNeurons = 0;
        _config.nbrInputNeurons = 0;

        _config.weights.clear();
        _config.biases.clear();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(_file_name));
            final NodeList childNodes = doc.getChildNodes();
            for (int i=0; i < childNodes.getLength(); i++) {
                Node subnode = childNodes.item(i);
                loadNode(subnode, doc, _config);
            }

            _config.nbrHiddenLayers -= 1;//in pmml format, outputlayer is a "layer", not here
            _config.nbrNeuronsPerHiddenLayer -= _config.nbrOutputNeurons;
            _config.nbrNeuronsPerHiddenLayer /= _config.nbrHiddenLayers;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        System.out.println(""+_config.nbrInputNeurons+" "+_config.nbrOutputNeurons+" "+_config.nbrHiddenLayers+" "+_config.nbrNeuronsPerHiddenLayer);
    }

    public static void loadNode(final Node _child, final Node _parent, final NeuralNetConfiguration _config){
        // il faut connaitre le nombre couches, inputs, outputs, etc..
        //ensuite refaire une passe pour set les poids selon les coordonnees

        // if node == NeuralNet -> nbrLayers (1+ hidden)
        // neuralInputs -> construire la couche d'input

        // neuralOutputs -> construire la couche d'output

        // neuralLayer -> construire les couches cachees

        // refaire une passe pour loader les poids ?

        //System.out.println(_child.getNodeName());

        final Map<String, String> attributes = readAttributes(_child);

        if("Neuron".equals(_child.getNodeName()) || "NeuralInput".equals(_child.getNodeName())){
            final String bias_as_string = attributes.get("bias");
            {
                final String id_as_string = attributes.get("id");
                final String[] layer_and_position = id_as_string.split(",");
                final int layer_id = Integer.parseInt(layer_and_position[0]);
                final int position_id = Integer.parseInt(layer_and_position[1]);
                if (!_config.biases.containsKey(layer_id)) {
                    _config.biases.put(layer_id, new HashMap<>());
                }
                _config.biases.get(layer_id).put(position_id, Double.parseDouble(bias_as_string));
            }
            if("Neuron".equals(_child.getNodeName())) _config.nbrNeuronsPerHiddenLayer += 1;
        }

        if("Con".equals(_child.getNodeName())){
            final String weight_as_string = attributes.get("weight");
            final String from_as_string = attributes.get("from");
            final String[] from_layer_and_position = from_as_string.split(",");
            final int from_layer_id = Integer.parseInt(from_layer_and_position[0]);
            final int from_position_id = Integer.parseInt(from_layer_and_position[1]);

            final Map<String, String> attributes_parent = readAttributes(_parent);
            {
                final String parent_id_as_string = attributes_parent.get("id");
                final String[] layer_and_position = parent_id_as_string.split(",");
                final int layer_id = Integer.parseInt(layer_and_position[0]);
                final int position_id = Integer.parseInt(layer_and_position[1]);
                // ensure that parent exists in sparse matrix
                if (!_config.weights.containsKey(layer_id)) {
                    _config.weights.put(layer_id, new HashMap<>());
                }
                if(!_config.weights.get(layer_id).containsKey(position_id)){
                    _config.weights.get(layer_id).put(position_id, new HashMap<>());
                }
                // then, insert link to other neuron
                final Map<Integer, Map<Integer, Double>> weighted_connection = _config.weights.get(layer_id).get(position_id);
                if (!weighted_connection.containsKey(from_layer_id)) {
                    weighted_connection.put(from_layer_id, new HashMap<>());
                }
                weighted_connection.get(from_layer_id).put(from_position_id, Double.parseDouble(weight_as_string));
            }
        }
        if("NeuralLayer".equals(_child.getNodeName())){
            _config.nbrHiddenLayers += 1;
        }
        if("NeuralInputs".equals(_child.getNodeName())){
            final String nbr_inputs_as_string = attributes.get("numberOfInputs");
            final int nbr_inputs = Integer.parseInt(nbr_inputs_as_string);
            _config.nbrInputNeurons = nbr_inputs;
        }
        if("NeuralOutputs".equals(_child.getNodeName())){
            final String nbr_outputs_as_string = attributes.get("numberOfOutputs");
            final int nbr_outputs = Integer.parseInt(nbr_outputs_as_string);
            _config.nbrOutputNeurons = nbr_outputs;
        }



        //System.out.println(_child.get());
        if(_child.hasChildNodes()) {
            final NodeList childNodes = _child.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                final Node subnode = childNodes.item(i);
                loadNode(subnode, _child, _config);
            }
        }
    }

    public static Map<String, String> readAttributes(final Node _node){
        final Map<String, String> attributes_map = new HashMap<>();
        if(_node.hasAttributes()){
            final NamedNodeMap attributes = _node.getAttributes();
            for (int i=0; i < attributes.getLength(); i++) {
                final Node item = attributes.item(i);
                final String name = item.getNodeName();
                final String textContent = item.getTextContent();
                if(name.trim().length() != 0) {
                    //System.out.println(name + " => " + textContent);
                    attributes_map.put(name, textContent);
                }
            }
        }
        return attributes_map;
    }
}
