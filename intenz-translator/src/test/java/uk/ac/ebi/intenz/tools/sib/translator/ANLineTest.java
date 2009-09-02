/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;



/**
 * ANLineTest
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 13:34:04
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public class ANLineTest extends BaseLineTest {

   public void populateTests () {
      dataHolders.add(new DataHolder("1.1.1.18", "<ital>myo</ital>-inositol 2-dehydrogenase",
            "Myo-inositol 2-dehydrogenase") );
      dataHolders.add(new DataHolder("1.1.1.85",
            "<greek>beta</greek>-IPM dehydrogenase",
            "Beta-IPM dehydrogenase"));
      dataHolders.add(new DataHolder("1.3.1.3", "<greek>Delta</greek><smallsup>4</smallsup>-3-ketosteroid 5<greek>beta</greek>-reductase",
            "Delta(4)-3-ketosteroid 5-beta-reductase"));
      dataHolders.add(new DataHolder("1.1.1.90","<ital>p</ital>-hydroxybenzyl alcohol dehydrogenase",
            "p-hydroxybenzyl alcohol dehydrogenase"));
      dataHolders.add(new DataHolder("1.1.3.4",
             "<greek>beta</greek>-<stereo>D</stereo>-glucose:oxygen 1-oxido-reductase",
             "Beta-D-glucose:oxygen 1-oxido-reductase"));
      dataHolders.add(new DataHolder("1.1.99.11",
             "D-Fructose dehydrogenase",
             "D-fructose dehydrogenase"));
      dataHolders.add(new DataHolder("1.6.99.3",
             "<greek>beta</greek>-NADH dehydrogenase dinucleotide",
             "Beta-NADH dehydrogenase dinucleotide"));
      dataHolders.add(new DataHolder("1.7.2.1",
            "<ital>cd</ital>-cytochrome nitrite reductase",
            "cd-cytochrome nitrite reductase"));
      dataHolders.add(new DataHolder("1.10.2.2", "cytochrome <ital>bc</ital><smallsub>1</smallsub> complex",
            "Cytochrome bc1 complex"));
      dataHolders.add( new DataHolder("1.13.11.34","5<greek>Delta</greek>-lipoxygenase",
            "5-Delta-lipoxygenase"));
      dataHolders.add( new DataHolder("2.1.1.11",
            "(-)-<element>S</element>-adenosyl-<stereo>L</stereo>-methionine:magnesium-protoporphyrin IX methyltransferase",
            "(-)-S-adenosyl-L-methionine:magnesium-protoporphyrin IX methyltransferase"));
      dataHolders.add(new DataHolder("2.4.1.94",
             "<element>N</element>-GlcNAc transferase",
             "N-GlcNAc transferase"));
      dataHolders.add( new DataHolder("2.5.1.2",
               "thiamin:base 2-methyl-4-aminopyrimidine-5-methenyltransferase",
               "Thiamine:base 2-methyl-4-aminopyrimidine-5-methenyltransferase"));
      dataHolders.add(new DataHolder("2.7.8.13",
               "UDP-MurNAc-Ala-<greek>gamma</greek><stereo>D</stereo>Glu-Lys-<stereo>D</stereo>Ala-<stereo>D</stereo>Ala:undecaprenylphosphate transferase",
               "UDP-MurNAc-Ala-gamma-DGlu-Lys-DAla-DAla:undecaprenylphosphate transferase"));
      dataHolders.add(new DataHolder("2.8.2.8",
             "<element>N</element>-HSST",
             "N-HSST"));
      dataHolders.add( new DataHolder("3.4.21.68",
              "tPA",
              "tPA"));
      dataHolders.add( new DataHolder("3.4.21.72",
               "Species variants differing slightly in specificity are secreted by Gram-negative bacteria <ital>Neisseria gonorrhoeae</ital> and <ital>Haemophilus influenzae</ital>",
               "Species variants differing slightly in specificity are secreted by Gram-negative bacteria Neisseria gonorrhoeae and Haemophilus influenzae"));
      dataHolders.add( new DataHolder("3.4.22.53",
              "m-calpain",
              "M-calpain"));
      dataHolders.add( new DataHolder("3.6.1.40",
             "pppGpp 5'-phosphohydrolase",
             "pppGpp 5'-phosphohydrolase"));
   }

    public ANLineTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

}
