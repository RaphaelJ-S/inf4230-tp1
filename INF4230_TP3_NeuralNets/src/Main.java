import helpers.DatasetReader;
import helpers.LabelledRecord;
import helpers.Normalization;
import helpers.pmml.PMMLReader;
import neuralnets.NeuralNet;

import java.io.PrintStream;
import java.util.*;

import static init.Initialisation.demarrer_apprentissage_et_tests;

public class Main {
    public static void main(final String[] _args){
        demarrer_apprentissage_et_tests(_args, System.out, true);
    }
}
