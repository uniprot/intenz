/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;


/**
 * CCLineTest
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 15:04:18
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public class CCLineTest extends BaseLineTest {

   public void populateTests () {
      dataHolders.add(new DataHolder("1.3.7.5",
            "Catalyses the four-electron reduction of biliverdin IX<greek>alpha</greek> (2-electron reduction at both the A and D rings)",
            "Catalyzes the four-electron reduction of biliverdin IX-alpha (2-electron reduction at both the A and D rings)"));
      dataHolders.add(new DataHolder("1.1.1.112",
            "3(20)<greek>alpha</greek>-Hydroxysteroids are also oxidized, more slowly",
            "3(20)-alpha-hydroxysteroids are also oxidized, more slowly"));
      dataHolders.add(new DataHolder("1.1.1.211",
             "Acts most rapidly on derivatives with chain length C<smallsub>8</smallsub> or C<smallsub>10</smallsub> (<ital>cf.</ital> EC 1.1.1.35)",
             "Acts most rapidly on derivatives with chain length C(8) or C(10) (cf. EC 1.1.1.35)"));
      dataHolders.add(new DataHolder("1.1.1.287",
             "<stereo>D</stereo>-Arabinitol is capable of quenching reactive oxygen species involved in defense reactions of the host plant.",
             "D-arabinitol is capable of quenching reactive oxygen species involved in defense reactions of the host plant."));
      dataHolders.add(new DataHolder("1.1.1.163",
            "4-Methylcyclohexanol and cyclohexanol can also act as substrates",
            "4-methylcyclohexanol and cyclohexanol can also act as substrates"));
      dataHolders.add(new DataHolder("1.1.99.24",
              "4-Hydroxybutanoate and (<stereo>R</stereo>)-2-hydroxyglutarate can also act as donors",
              "4-hydroxybutanoate and (R)-2-hydroxyglutarate can also act as donors"));
      dataHolders.add(new DataHolder("1.1.99.29",
              "1,4-Benzoquinone or ferricenium",
              "1,4-benzoquinone or ferricenium"));
      dataHolders.add(new DataHolder("1.1.99.29",
              "<stereo>D</stereo>-Glucose is exclusively",
              "D-glucose is exclusively"));
      dataHolders.add(new DataHolder("1.3.1.31",
            "Acts, in the reverse direction, on a wide range of alkyl and aryl <greek>alpha</greek><greek>beta</greek>-unsaturated carboxylate ions",
            "Acts, in the reverse direction, on a wide range of alkyl and aryl alpha-beta-unsaturated carboxylate ions"));
      dataHolders.add(new DataHolder("1.5.3.11",
            "Also acts on <element>N</element><smallsup>1</smallsup>-acetylspermidine and <element>N</element><smallsup>1</smallsup>,<element>N</element><smallsup>12</smallsup>-diacetylspermine",
            "Also acts on N(1)-acetylspermidine and N(1),N(12)-diacetylspermine"));
      dataHolders.add(new DataHolder("1.7.2.1",
            "Cytochrome <ital>cd</ital><smallsub>1</smallsub> also has oxidase and hydroxylamine reductase activities.",
            "Cytochrome cd1 also has oxidase and hydroxylamine reductase activities."));
      dataHolders.add(new DataHolder("1.8.3.5",
            "<element>N</element>-Acetyl-prenylcysteine and prenylcysteinyl peptides are not substrates",
            "N-acetyl-prenylcysteine and prenylcysteinyl peptides are not substrates"));
      dataHolders.add(new DataHolder("1.10.99.1",
            "A cytochrome <ital>f</ital>,<ital>b</ital><smallsub>6</smallsub> complex separated from chloroplasts",
            "A cytochrome f,b6 complex separated from chloroplasts"));
      dataHolders.add(new DataHolder("1.11.1.15",
             "They can be divided into three classes: typical 2-Cys, atypical 2-Cys and 1-Cys peroxiredoxins",
             "They can be divided into three classes: typical 2-Cys, atypical 2-Cys and 1-Cys peroxiredoxins"));
      dataHolders.add(new DataHolder("1.11.1.15",
             "The peroxidase reaction comprises two steps centred around a redox-active cysteine called the peroxidatic cysteine.",
             "The peroxidase reaction comprises two steps centered around a redox-active cysteine called the peroxidatic cysteine."));
      dataHolders.add( new DataHolder("1.12.2.1",
             "Some of the enzyme contain nickel ([NiFe]-hydrogenases) and, of these, some contain selenocysteine ([NiFeSe]-hydrogenases)",
             "Some of the enzyme contain nickel ([NiFe]-hydrogenases) and, of these, some contain selenocysteine ([NiFeSe]-hydrogenases)"));
      dataHolders.add(new DataHolder("1.13.12.6",
              "<ital>Cypridina</ital> luciferin is [3-[3,7-dihydro-6-(1<element>H</element>-indol-3-yl)-2-[(<stereo>S</stereo>)-1-methyl-6-propyl]-3-oxoimidazo-[1,2-<ital>a</ital>]pyrazin-8-yl]propyl]guanidine.",
              "Cypridina luciferin is (3-(3,7-dihydro-6-(1H-indol-3-yl)-2-((S)-1-methyl-6-propyl)-3-oxoimidazo-[1,2-a]pyrazin-8-yl)propyl)guanidine."));
      dataHolders.add(new DataHolder("1.16.1.2",
             "transferrin-[Fe<smallsup>2+</smallsup>]<smallsub>2</smallsub> + NAD<smallsup>+</smallsup> = transferrin-[Fe<smallsup>3+</smallsup>]<smallsub>2</smallsub> + NADH + H<smallsup>+</smallsup>",
             "Transferrin-[Fe(2+)](2) + NAD(+) = transferrin-[Fe(3+)](2) + NADH"));
      dataHolders.add(new DataHolder("1.16.1.8",
             "The substrate of the enzyme is the inactivated [Co(II)] form of EC <a href=\"searchEc.do?ec=2.1.1.13&view=INTENZ\">2.1.1.13</a>.",
             "The substrate of the enzyme is the inactivated [Co(II)] form of EC 2.1.1.13."));
      dataHolders.add(new DataHolder("1.17.3.2",
            "Under some conditions the product is mainly superoxide rather than peroxide: R-H + H<smallsub>2</smallsub>O + <b>2</b> O<smallsub>2</smallsub> = ROH + <b>2</b> O<smallsub>2</smallsub><smallsup><radical_dot/>-</smallsup> + 2 H<smallsup>+</smallsup>",
            "Under some conditions the product is mainly superoxide rather than peroxide: R-H + H(2)O + 2 O(2) = ROH + 2 O(2)(.-) + 2 H(+)"));
//      dataHolders.add(new DataHolder("2.1.1.41",
//            "<element>S</element>-Adenosyl-<stereo>L</stereo>-methionine attacks the <ital>Si</ital>-face of the 24(25) double bond and the C-24 hydrogen is transferred to C-25 on the <ital>Re</ital> face of the double bond.",
//            "S-adenosyl-L-methionine attacks the Si face of the 24(25) double bond and the C-24 hydrogen is transferred to C-25 on the Re face of the double bond."));
      dataHolders.add(new DataHolder("2.1.1.98",
               "2-(3-Carboxy-3-(methylammonio)propyl)-L-histidine and the corresponding dimethyl compound can also act as acceptors.",
               "2-(3-carboxy-3-(methylammonio)propyl)-L-histidine and the corresponding dimethyl compound can also act as acceptors."));
      dataHolders.add(new DataHolder("2.3.1.23",
               "1-Acyl-<ital>sn</ital>-glycero-3-phosphoinositol can also act as acceptor",
               "1-acyl-sn-glycero-3-phosphoinositol can also act as acceptor"));
      dataHolders.add(new DataHolder("2.4.1.118",
            "Acts on a range of <element>N</element><smallsup>6</smallsup>-substituted adenines, including <a href=\"http://www.chem.qmul.ac.uk/iubmb/enzyme/glossary/zeatin.html\">zeatin</a> [<element>N</element><smallsup>6</smallsup>-(4-hydroxy-3-methylbut-<stereo>trans</stereo>-2-enylamino)purine] and <element>N</element><smallsup>6</smallsup>-benzylaminopurine, but not <element>N</element><smallsup>6</smallsup>-benzyladenine",
            "Acts on a range of N(6)-substituted adenines, including zeatin (N(6)-(4-hydroxy-3-methylbut-trans-2-enylamino)purine) and N(6)-benzylaminopurine, but not N(6)-benzyladenine"));
      dataHolders.add(new DataHolder("2.4.1.127",
            "(+)-Neomenthol can also act as acceptor",
            "(+)-neomenthol can also act as acceptor"));
      dataHolders.add(new DataHolder("2.4.1.214",
            "This enzyme catalyses a reaction similar to that of EC 2.4.1.68, but transferring the <stereo>L</stereo>-fucosyl group from GDP-<greek>beta</greek>-<stereo>L</stereo>-fucose to form an <greek>alpha</greek>1,3-linkage rather than an <greek>alpha</greek>1,6-linkage. The <element>N</element>-glycan products of this enzyme are present in plants, insects and some other invertebrates (e.g., <ital>Schistosoma</ital>, <ital>Haemonchus</ital>, <ital>Lymnaea</ital>)",
            "This enzyme catalyzes a reaction similar to that of EC 2.4.1.68, but transferring the L-fucosyl group from GDP-beta-L-fucose to form an alpha-1,3-linkage rather than an alpha-1,6-linkage. The N-glycan products of this enzyme are present in plants, insects and some other invertebrates (e.g., Schistosoma, Haemonchus, Lymnaea)"));
      dataHolders.add(new DataHolder("2.6.1.56",
            "<stereo>L</stereo>-Glutamate and <stereo>L</stereo>-glutamine can also act as amino donors",
            "L-glutamate and L-glutamine can also act as amino donors"));
      dataHolders.add(new DataHolder("2.6.1.76",
            "Involved in the formation of 1,3-diaminopropane in <ital>Haemophilus influenzae</ital> and <ital>Acinetobacter baumannii</ital>",
            "Involved in the formation of 1,3-diaminopropane in Haemophilus influenzae and Acinetobacter baumannii"));
      // RA 2005-08-08
      dataHolders.add( new DataHolder("2.7.1.88",
             "3'-Deoxydihydrostreptomycin 6-phosphate can also act as acceptor.",
             "3'-deoxydihydrostreptomycin 6-phosphate can also act as acceptor."));
      dataHolders.add( new DataHolder("2.7.3.2",
             "<element>N</element>-Ethylglycocyamine can also act as acceptor",
             "N-ethylglycocyamine can also act as acceptor"));
      dataHolders.add( new DataHolder("2.7.8.21",
               "<greek>beta</greek>-Linked glucose residues in simple glucosides, such as gentiobiose, can act as acceptors",
               "Beta-linked glucose residues in simple glucosides, such as gentiobiose, can act as acceptors"));
      dataHolders.add( new DataHolder("2.7.8.25",
              "2'-(5''-Triphosphoribosyl)-3'-dephospho-CoA is the precursor of the",
              "2'-(5''-triphosphoribosyl)-3'-dephospho-CoA is the precursor of the"));
      dataHolders.add(new DataHolder("3.1.4.43",
          "This enzyme also hydrolyses Ins(cyclic 1,2)<element>P</element> to Ins-1-<element>P</element>",
          "This enzyme also hydrolyzes Ins(cyclic 1,2)P to Ins-1-P"));
	  dataHolders.add(new DataHolder("3.4.22.55",
		  "Apart from itself, the enzyme can cleave golgin-16, which is present in the Golgi complex and has a cleavage site that is unique for caspase-2.",
		  "Apart from itself, the enzyme can cleave golgin-16, which is present in the Golgi complex and has a cleavage site that is unique for caspase-2."));
      dataHolders.add( new DataHolder("3.4.24.7",
               "<greek>alpha</greek>-macroglobulins are cleaved much more rapidly.",
               "Alpha-macroglobulins are cleaved much more rapidly."));
      dataHolders.add( new DataHolder("3.4.23.19",
            "Isolated from <ital>Aspergillus niger</ital> var. <ital>macrosporus</ital>, distinct from aspergillopepsin I in specificity and insensitivity to pepstatin",
            "Isolated from Aspergillus niger var. macrosporus, distinct from aspergillopepsin I in specificity and insensitivity to pepstatin"));
      dataHolders.add(new DataHolder("3.6.5.3",
            "In the elongation phase, the GTP-hydrolysing proteins are the EF-Tu polypeptide of the prokaryotic transfer factor (43 kDa), the eukaryotic elongation factor EF-1<greek>alpha</greek> (53 kDa), the prokaryotic EF-G (77 kDa), the eukaryotic EF-2 (70-110 kDa) and the signal recognition particle that play a role in endoplasmic reticulum protein synthesis (325 kDa)",
            "In the elongation phase, the GTP-hydrolyzing proteins are the EF-Tu polypeptide of the prokaryotic transfer factor (43 kDa), the" +
            " eukaryotic elongation factor EF-1-alpha (53 kDa), the prokaryotic EF-G (77 kDa), the eukaryotic EF-2 (70-110 kDa) and the signal" +
            " recognition particle that play a role in endoplasmic reticulum protein synthesis (325 kDa)"));
       // RA 2005-08-09
      dataHolders.add( new DataHolder("4.2.1.3",
               "<stereo>cis</stereo>-aconitate is used to designate the isomer (<stereo>Z</stereo>)-prop-1-ene-1,2,3-tricarboxylate.",
               "Cis-aconitate is used to designate the isomer (Z)-prop-1-ene-1,2,3-tricarboxylate."));
      dataHolders.add(new DataHolder("4.2.1.4",
            "<stereo>cis</stereo>-Aconitate is used to designate the isomer (<stereo>Z</stereo>)-prop-1-ene-1,2,3-tricarboxylate.",
            "Cis-aconitate is used to designate the isomer (Z)-prop-1-ene-1,2,3-tricarboxylate."));
      dataHolders.add(new DataHolder("4.2.2.5",
    		  "<bracket>open</bracket>-4<parenthesis>close</parenthesis>GlcA(<stereo>beta</stereo>1-3)GalNAc<parenthesis>open</parenthesis><greek>beta</greek>1-<bracket>close</bracket><smallsub><ital>n</ital></smallsub>",
    		  "[-4)GlcA(beta-1-3)GalNAc(beta-1-](n)"));
      dataHolders.add(new DataHolder("4.2.2.19",
    		  "[-4<parenthesis>close</parenthesis>GlcA(<stereo>beta</stereo>1-3)GalNAc<parenthesis>open</parenthesis><stereo>beta</stereo>1-]<smallsub><ital>n</ital></smallsub>",
    		  "[-4)GlcA(beta-1-3)GalNAc(beta-1-](n)"));
      dataHolders.add(new DataHolder("4.2.2.19",
    		  "[-4)GlcA(<stereo>beta</stereo>1-3)GalNAc(<stereo>beta</stereo>1-]<smallsub><ital>n</ital></smallsub>",
    		  "[-4)GlcA(beta-1-3)GalNAc(beta-1-](n)"));

      dataHolders.add(new DataHolder("No EC, just testing insertion of hyphens...",
    		  "X<stereo>alpha</stereo> 1<stereo>alpha</stereo> (<stereo>alpha</stereo> )<stereo>alpha</stereo> <ital>n</ital><stereo>alpha</stereo> <parenthesis>close</parenthesis><stereo>alpha</stereo> <stereo>alpha</stereo>",
    		  "X-alpha 1-alpha (alpha )-alpha n-alpha )alpha alpha"));
      dataHolders.add(new DataHolder("No EC, just testing insertion of hyphens...",
    		  "X<stereo>beta</stereo> 1<stereo>beta</stereo> (<stereo>beta</stereo> )<stereo>beta</stereo> <ital>n</ital><stereo>beta</stereo> <parenthesis>close</parenthesis><stereo>beta</stereo> <stereo>beta</stereo>",
    		  "X-beta 1-beta (beta )-beta n-beta )beta beta"));
      dataHolders.add(new DataHolder("No EC, just testing insertion of hyphens...",
    		  "X<stereo>L</stereo> 1<stereo>L</stereo> (<stereo>L</stereo> )<stereo>L</stereo> <ital>n</ital><stereo>L</stereo> <parenthesis>close</parenthesis><stereo>L</stereo> <stereo>L</stereo>",
    		  "XL 1L (L )L nL )L L"));
   }

  public CCLineTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }
}
