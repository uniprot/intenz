/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.xchars.domain.EncodingType;
import org.apache.log4j.Logger;
import java.util.Iterator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;


/**
 * CALineTest is used to test various reaction lines.
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 10:42:30
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public class CALineTest extends BaseLineTest {

   Logger LOGGER = Logger.getLogger(CALineTest.class);


   public void populateTests(){
      dataHolders.add(new DataHolder("1.1.1.3", "<stereo>L</stereo>-homoserine + NAD(P)<smallsup>+</smallsup> = <stereo>L</stereo>-aspartate 4-semialdehyde + NAD(P)H + H<smallsup>+</smallsup>",
      "L-homoserine + NAD(P)(+) = L-aspartate 4-semialdehyde + NAD(P)H") );
      dataHolders.add(new DataHolder("1.1.1.21",
        "alditol + NAD(P)<smallsup>+</smallsup> = aldose + NAD(P)H + H<smallsup>+</smallsup>",
        "Alditol + NAD(P)(+) = aldose + NAD(P)H"));
      dataHolders.add(new DataHolder("1.1.1.22", "UDP-glucose + <b>2</b> NAD<smallsup>+</smallsup> + H<smallsub>2</smallsub>O = UDP-glucuronate + <b>2</b> NADH + <b>2</b> H<smallsup>+</smallsup>",
      "UDP-glucose + 2 NAD(+) + H(2)O = UDP-glucuronate + 2 NADH"));
      dataHolders.add(new DataHolder("1.1.1.34", "(<stereo>R</stereo>)-mevalonate + CoA + <b>2</b> NADP<smallsup>+</smallsup> = (<stereo>S</stereo>)-3-hydroxy-3-methylglutaryl-CoA + <b>2</b> NADPH + <b>2</b> H<smallsup>+</smallsup>",
      "(R)-mevalonate + CoA + 2 NADP(+) = (S)-3-hydroxy-3-methylglutaryl-CoA + 2 NADPH"));
      dataHolders.add(new DataHolder("1.1.1.62", "estradiol-17<greek>beta</greek> + NAD(P)<smallsup>+</smallsup> = estrone + NAD(P)H + H<smallsup>+</smallsup>",
      "Estradiol-17-beta + NAD(P)(+) = estrone + NAD(P)H"));
      dataHolders.add(new DataHolder("1.1.1.62","estradiol-17<greek>beta</greek> + NAD(P)<smallsup>+</smallsup> = estrone + NAD(P)H + H<smallsup>+</smallsup>",
      "Estradiol-17-beta + NAD(P)(+) = estrone + NAD(P)H"));
      dataHolders.add(new DataHolder("1.1.1.92", "<stereo>D</stereo>-glycerate + NAD(P)<smallsup>+</smallsup> + CO<smallsub>2</smallsub> = 2-hydroxy-3-oxosuccinate + NAD(P)H + <b>2</b> H<smallsup>+</smallsup>",
      "D-glycerate + NAD(P)(+) + CO(2) = 2-hydroxy-3-oxosuccinate + NAD(P)H"));
      dataHolders.add(new DataHolder("1.1.1.100",
	     "(3<stereo>R</stereo>)-3-hydroxyacyl-<protein>acyl-carrier-protein</protein> + NADP<smallsup>+</smallsup> = 3-oxoacyl-<protein>acyl-carrier-protein</protein> + NADPH + H<smallsup>+</smallsup>",
  		 "(3R)-3-hydroxyacyl-[acyl-carrier-protein] + NADP(+) = 3-oxoacyl-[acyl-carrier-protein] + NADPH"));
      dataHolders.add(new DataHolder("1.1.1.145",
		  	"a 3<greek>beta</greek>-hydroxy-<greek>Delta</greek><smallsup>5</smallsup>-steroid + NAD<smallsup>+</smallsup> = a 3-oxo-<greek>Delta</greek><smallsup>5</smallsup>-steroid + NADH + H<smallsup>+</smallsup>",
      		"A 3-beta-hydroxy-Delta(5)-steroid + NAD(+) = a 3-oxo-Delta(5)-steroid + NADH"));
      dataHolders.add(new DataHolder("1.1.1.160","(<plusmn/>)-5-[(<ital>tert</ital>-butylamino)-2'-hydroxypropoxy]-1,2,3,4-tetrahydro-1-naphthol + NADP<smallsup>+</smallsup> = (<plusmn/>)-5-[(<ital>tert</ital>-butylamino)-2'-hydroxypropoxy]-3,4-dihydro-1(2<element>H</element>)-naphthalenone + NADPH + H<smallsup>+</smallsup>",
      "(+-)-5-((tert-butylamino)-2'-hydroxypropoxy)-1,2,3,4-tetrahydro-1-naphthol + NADP(+) = (+-)-5-((tert-butylamino)-2'-hydroxypropoxy)-3,4-dihydro-1(2H)-naphthalenone + NADPH"));
      dataHolders.add(new DataHolder("1.1.1.177",
             "<ital>sn</ital>-glycerol 3-phosphate + NADP<smallsup>+</smallsup> = <stereo>D</stereo>-glyceraldehyde 3-phosphate + NADPH + H<smallsup>+</smallsup>",
             "sn-glycerol 3-phosphate + NADP(+) = D-glyceraldehyde 3-phosphate + NADPH"));
      dataHolders.add(new DataHolder("1.1.1.261",
             "<ital>sn</ital>-glycerol-1-phosphate + NAD(P)<smallsup>+</smallsup> = glycerone phosphate + NAD(P)H + H<smallsup>+</smallsup>",
             "sn-glycerol-1-phosphate + NAD(P)(+) = glycerone phosphate + NAD(P)H"));
      dataHolders.add(new DataHolder("1.2.2.2", "pyruvate + ferricytochrome <ital>b</ital><smallsub>1</smallsub> + H<smallsub>2</smallsub>O = acetate + CO<smallsub>2</smallsub> + ferrocytochrome <ital>b</ital><smallsub>1</smallsub>",
      "Pyruvate + ferricytochrome b1 + H(2)O = acetate + CO(2) + ferrocytochrome b1"));
      dataHolders.add(new DataHolder("1.3.1.42", "8-[(1<stereo>R</stereo>,2<stereo>R</stereo>)-3-oxo-2-{(<stereo>Z</stereo>)-pent-2-enyl}cyclopentyl]octanoate + NADP<smallsup>+</smallsup> = (15<stereo>Z</stereo>)-12-oxophyto-10,15-dienoate + NADPH + H<smallsup>+</smallsup>",
      "8-((1R,2R)-3-oxo-2-((Z)-pent-2-enyl)cyclopentyl)octanoate + NADP(+) = (15Z)-12-oxophyto-10,15-dienoate + NADPH"));
      dataHolders.add(new DataHolder("1.6.2.5", "NADPH + H<smallsup>+</smallsup> + <b>2</b> ferricytochrome <ital>c</ital><smallsub>2</smallsub> = NADP<smallsup>+</smallsup> + <b>2</b> ferrocytochrome <ital>c</ital><smallsub>2</smallsub>",
      "NADPH + 2 ferricytochrome c2 = NADP(+) + 2 ferrocytochrome c2"));
      dataHolders.add(new DataHolder("1.7.1.4", "ammonium hydroxide + <b>3</b> NAD(P)<smallsup>+</smallsup> + H<smallsub>2</smallsub>O = nitrite + <b>3</b> NAD(P)H + <b>3</b> H<smallsup>+</smallsup>",
      "Ammonium hydroxide + 3 NAD(P)(+) + H(2)O = nitrite + 3 NAD(P)H"));
      dataHolders.add( new DataHolder("1.13.12.5","<ital>Renilla</ital> luciferin + O<smallsub>2</smallsub> = oxidized <ital>Renilla</ital> luciferin + CO<smallsub>2</smallsub> + h<greek>nu</greek>",
      "Renilla luciferin + O(2) = oxidized Renilla luciferin + CO(2) + light"));
      dataHolders.add(new DataHolder("1.14.13.39", "<stereo>L</stereo>-arginine + <b>n</b> NADPH + <b>n</b> H<smallsup>+</smallsup> + <b>m</b> O<smallsub>2</smallsub> = citrulline + nitric oxide + <b>n</b> NADP<smallsup>+</smallsup>",
      "L-arginine + n NADPH + m O(2) = citrulline + nitric oxide + n NADP(+)"));
      dataHolders.add(new DataHolder("1.14.13.70", "obtusifoliol + <b>3</b> O<smallsub>2</smallsub> + <b>3</b> NADPH + <b>3</b> H<smallsup>+</smallsup> = 4<greek>alpha</greek>-methyl-5<greek>alpha</greek>-ergosta-8,14,24(28)-trien-3<greek>beta</greek>-ol + formate + <b>3</b> NADP<smallsup>+</smallsup> + <b>3</b> H<smallsub>2</smallsub>O",
      "Obtusifoliol + 3 O(2) + 3 NADPH = 4-alpha-methyl-5-alpha-ergosta-8,14,24(28)-trien-3-beta-ol + formate + 3 NADP(+) + 3 H(2)O"));
      dataHolders.add(new DataHolder("1.14.13.78", "<ital>ent</ital>-kaur-16-en-19-al + NADPH + O<smallsub>2</smallsub> = <ital>ent</ital>-kaur-16-en-19-oate + NADP<smallsup>+</smallsup> + H<smallsub>2</smallsub>O",
      "Ent-kaur-16-en-19-al + NADPH + O(2) = ent-kaur-16-en-19-oate + NADP(+) + H(2)O"));
      dataHolders.add(new DataHolder("1.17.4.3", "(<stereo>E</stereo>)-4-hydroxy-3-methylbut-2-en-1-yl diphosphate + H<smallsub>2</smallsub>O + protein-disulfide = 2-<element>C</element>-methyl-<stereo>D</stereo>-erythritol 2,4-cyclodiphosphate + protein-dithiol",
      "(E)-4-hydroxy-3-methylbut-2-en-1-yl diphosphate + H(2)O + protein-disulfide = 2-C-methyl-D-erythritol 2,4-cyclodiphosphate + protein-dithiol"));
      dataHolders.add(new DataHolder("2.1.1.44", "<element>S</element>-adenosyl-<stereo>L</stereo>-methionine + <element>N</element><smallsup><greek>alpha</greek></smallsup>,<element>N</element><smallsup><greek>alpha</greek></smallsup>-dimethyl-<stereo>L</stereo>-histidine = <element>S</element>-adenosyl-<stereo>L</stereo>-homocysteine + <element>N</element><smallsup><greek>alpha</greek></smallsup>,<element>N</element><smallsup><greek>alpha</greek></smallsup>,<element>N</element><smallsup><greek>alpha</greek></smallsup>-trimethyl-<stereo>L</stereo>-histidine",
      "S-adenosyl-L-methionine + N(alpha),N(alpha)-dimethyl-L-histidine = S-adenosyl-L-homocysteine + N(alpha),N(alpha),N(alpha)-trimethyl-L-histidine"));
      dataHolders.add(new DataHolder("2.1.1.56","<element>S</element>-adenosyl-<stereo>L</stereo>-methionine + G(5')pppR-RNA = <element>S</element>-adenosyl-<stereo>L</stereo>-homocysteine + m<smallsup>7</smallsup>G(5')pppR-RNA",
      "S-adenosyl-L-methionine + G(5')pppR-RNA = S-adenosyl-L-homocysteine + m(7)G(5')pppR-RNA"));
      dataHolders.add(new DataHolder("2.1.1.57", "<element>S</element>-adenosyl-<stereo>L</stereo>-methionine + m<smallsup>7</smallsup>G(5')pppR-RNA = <element>S</element>-adenosyl-<stereo>L</stereo>-homocysteine + m<smallsup>7</smallsup>G(5')pppRm-RNA",
      "S-adenosyl-L-methionine + m(7)G(5')pppR-RNA = S-adenosyl-L-homocysteine + m(7)G(5')pppRm-RNA"));
      dataHolders.add(new DataHolder("2.1.1.62","<element>S</element>-adenosyl-<stereo>L</stereo>-methionine + m<smallsup>7</smallsup>G(5')pppAm = <element>S</element>-adenosyl-<stereo>L</stereo>-homocysteine + m<smallsup>7</smallsup>G(5')pppm<smallsup>6</smallsup>Am",
      "S-adenosyl-L-methionine + m(7)G(5')pppAm = S-adenosyl-L-homocysteine + m(7)G(5')pppm(6)Am"));
//      dataHolders.add(new DataHolder("2.1.1.74", "5,10-methylenetetrahydrofolate + tRNA U<smallsub><greek>Psi</greek></smallsub>C + FADH<smallsub>2</smallsub> = tetrahydrofolate + tRNA T<smallsub><greek>Psi</greek></smallsub>C + FAD",
//             "5,10-methylenetetrahydrofolate + tRNA U-psi-C + FADH(2) = tetrahydrofolate + tRNA T-psi-C + FAD"));
      dataHolders.add(new DataHolder("2.3.1.85",
              "acetyl-CoA + <b><ital>n</ital></b> malonyl-CoA + <b>2<ital>n</ital></b> NADPH + <b>2<ital>n</ital></b> H<smallsup>+</smallsup> = a long-chain fatty acid + <b>(<ital>n</ital>+1)</b> CoA + <b><ital>n</ital></b> CO<smallsub>2</smallsub> + <b>2<ital>n</ital></b> NADP<smallsup>+</smallsup>",
              "Acetyl-CoA + n malonyl-CoA + 2n NADPH = a long-chain fatty acid + (n+1) CoA + n CO(2) + 2n NADP(+)"));
      dataHolders.add(new DataHolder("2.3.1.59", "acetyl-CoA + gentamicin C<smallsub>1a</smallsub> = CoA + <element>N</element><smallsup>2'</smallsup>-acetylgentamicin C<smallsub>1a</smallsub>",
             "Acetyl-CoA + gentamicin C(1a) = CoA + N(2')-acetylgentamicin C(1a)"));
      dataHolders.add( new DataHolder("2.3.2.12","peptidyl-tRNA<smallsub>1</smallsub> + aminoacyl-tRNA<smallsub>2</smallsub> = tRNA<smallsub>1</smallsub> + peptidyl(aminoacyl-tRNA<smallsub>2</smallsub>)",
             "Peptidyl-tRNA(1) + aminoacyl-tRNA(2) = tRNA(1) + peptidyl(aminoacyl-tRNA(2))"));
      dataHolders.add(new DataHolder("2.4.1.92",
    		  "1-<element>O</element>-[<element>O</element>-2-(acetylamino)-2-deoxy-<stereo>beta</stereo>-<stereo>D</stereo>-galactopyranosyl-(1<arrow>right</arrow>4)-<element>O</element>-[<element>N</element>-acetyl-<stereo>alpha</stereo>-neuraminosyl-(2<arrow>right</arrow>3)]-<element>O</element>-<stereo>beta</stereo>-<stereo>D</stereo>-galactopyranosyl-(1<arrow>right</arrow>4)-<stereo>beta</stereo>-<stereo>D</stereo>-glucopyranosyl]-ceramide",
    		  "1-O-(O-2-(acetylamino)-2-deoxy-beta-D-galactopyranosyl-(1->4)-O-(N-acetyl-alpha-neuraminosyl-(2->3))-O-beta-D-galactopyranosyl-(1->4)-beta-D-glucopyranosyl)-ceramide"));
      //todo: find result of this
//      dataHolders.add( new DataHolder("2.4.1.100",
//            "[<greek>beta</greek>-<stereo>D</stereo>-fructosyl-(2<arrow>right</arrow>1)-]<smallsub><ital>m</ital></smallsub> + [<greek>beta</greek>-<stereo>D</stereo>-fructosyl-(2<arrow>right</arrow>1)-]<smallsub><ital>n</ital></smallsub> = [<greek>beta</greek>-<stereo>D</stereo>-fructosyl-(2<arrow>right</arrow>1)-]<smallsub><ital>m</ital>-1</smallsub> + [<greek>beta</greek>-<stereo>D</stereo>-fructosyl-(2<arrow>right</arrow>1)-]<smallsub><ital>n</ital>+1</smallsub>",
//            "(Beta-D-fructosyl-(2->1)-)(m) + (beta-D-fructosyl-(2->1)-)(n) = (beta-D-fructosyl-(2->1)-)(m-1) + (beta-D-fructosyl-(2->1)-)(n+1)"));
      dataHolders.add( new DataHolder("2.4.1.155",
             "UDP-<element>N</element>-acetyl-<stereo>D</stereo>-glucosamine + 6-(2-[<element>N</element>-acetyl-<greek>beta</greek>-<stereo>D</stereo>-glucosaminyl]-<greek>alpha</greek>-<stereo>D</stereo>-mannosyl)-<greek>beta</greek>-<stereo>D</stereo>-mannosyl-R = UDP + 6-(2,6-bis[<element>N</element>-acetyl-<greek>beta</greek>-<stereo>D</stereo>-glucosaminyl]-<greek>alpha</greek>-<stereo>D</stereo>-mannosyl)-<greek>beta</greek>-<stereo>D</stereo>-mannosyl-R",
             "UDP-N-acetyl-D-glucosamine + 6-(2-(N-acetyl-beta-D-glucosaminyl)-alpha-D-mannosyl)-beta-D-mannosyl-R = UDP + 6-(2,6-bis(N-acetyl-beta-D-glucosaminyl)-alpha-D-mannosyl)-beta-D-mannosyl-R"));
      dataHolders.add(new DataHolder("2.4.1.212","<b><ital>n</ital></b> UDP-<element>N</element>-acetyl-<stereo>D</stereo>-glucosamine + <b><ital>n</ital></b> UDP-<stereo>D</stereo>-glucuronate = [<greek>beta</greek>-<element>N</element>-acetyl-<stereo>D</stereo>-glucosaminyl(1<arrow>right</arrow>4)<greek>beta</greek>-<stereo>D</stereo>-glucuronosyl(1<arrow>right</arrow>3)]<smallsub><b><ital>n</ital></b></smallsub> + <b>2<ital>n</ital></b> UDP",
            "n UDP-N-acetyl-D-glucosamine + n UDP-D-glucuronate = (beta-N-acetyl-D-glucosaminyl(1->4)-beta-D-glucuronosyl(1->3))(n) + 2n UDP"));
      dataHolders.add(new DataHolder("2.4.2.30",
              "NAD<smallsup>+</smallsup> + (ADP-<stereo>D</stereo>-ribosyl)<smallsub><ital>n</ital></smallsub>-acceptor = nicotinamide + (ADP-<stereo>D</stereo>-ribosyl)<smallsub><ital>n</ital>+1</smallsub>-acceptor + H<smallsup>+</smallsup>",
              "NAD(+) + (ADP-D-ribosyl)(n)-acceptor = nicotinamide + (ADP-D-ribosyl)(n+1)-acceptor"));
      dataHolders.add( new DataHolder("2.4.99.7",
             "CMP-<element>N</element>-acetylneuraminate + <element>N</element>-acetyl-<greek>alpha</greek>-neuraminyl-(2<arrow>right</arrow>3)-<greek>beta</greek>-<stereo>D</stereo>-galactosyl-(1<arrow>right</arrow>3)-<element>N</element>-acetyl-<stereo>D</stereo>-galactosaminyl-R = CMP + <element>N</element>-acetyl-<greek>alpha</greek>-neuraminyl-(2<arrow>right</arrow>3)-<greek>beta</greek>-<stereo>D</stereo>-galactosyl-(1<arrow>right</arrow>3)-[<element>N</element>-acetyl-<greek>alpha</greek>-neuraminyl-(2<arrow>right</arrow>6)]-<element>N</element>-acetyl-<stereo>D</stereo>-galactosaminyl-R",
             "CMP-N-acetylneuraminate + N-acetyl-alpha-neuraminyl-(2->3)-beta-D-galactosyl-(1->3)-N-acetyl-D-galactosaminyl-R = CMP + N-acetyl-alpha-neuraminyl-(2->3)-beta-D-galactosyl-(1->3)-(N-acetyl-alpha-neuraminyl-(2->6))-N-acetyl-D-galactosaminyl-R"));
      dataHolders.add(new DataHolder("2.5.1.61", "<b>4</b> porphobilinogen + H<smallsub>2</smallsub>O = hydroxymethylbilane + <b>4</b> NH<smallsub>3</smallsub>",
      "4 porphobilinogen + H(2)O = hydroxymethylbilane + 4 NH(3)"));
      
      dataHolders.add(new DataHolder("2.7.7.50",
    		  "GTP + (5')ppPur-mRNA = diphosphate + G(5')pppPur-mRNA",
    		  "GTP + (5')ppPur-mRNA = diphosphate + G(5')pppPur-mRNA"));
      
      dataHolders.add(new DataHolder("2.7.8.7",
    		  "CoA-[4'-phosphopantetheine] + apo-<protein>acyl-carrier-protein</protein> = adenosine 3',5'-bisphosphate + holo-<protein>acyl-carrier-protein</protein>",
    		  "CoA-(4'-phosphopantetheine) + apo-[acyl-carrier-protein] = adenosine 3',5'-bisphosphate + holo-[acyl-carrier-protein]"));

      //todo: check that this has been corrected
      dataHolders.add(new DataHolder("3.1.3.56","<stereo>D</stereo>-<ital>myo</ital>-inositol 1,4,5-trisphosphate + H<smallsub>2</smallsub>O = <ital>myo</ital>-inositol 1,4-bisphosphate + phosphate",
             "D-myo-inositol 1,4,5-trisphosphate + H(2)O = myo-inositol 1,4-bisphosphate + phosphate"));
      dataHolders.add(new DataHolder("3.2.1.120",
             "Hydrolysis of 1,4-<greek>beta</greek>-<stereo>D</stereo>-glucosidic links in oligoxyloglucans so as to remove successive isoprimeverose (i.e. <greek>alpha</greek>-xylo-1,6-<greek>beta</greek>-<stereo>D</stereo>-glucosyl-) residues from the non-reducing chain ends",
             "Hydrolysis of 1,4-beta-D-glucosidic links in oligoxyloglucans so as to remove successive isoprimeverose (i.e. alpha-xylo-1,6-beta-D-glucosyl-) residues from the non-reducing chain ends"));
      dataHolders.add( new DataHolder("3.4.21.42",
            "Cleavage of Arg<scissile/>Ala bond in complement component C4 to form C4a and C4b, and Lys(or Arg)<scissile/>Lys bond in complement component C2 to form C2a and C2b: the <quotes/>classical<quotes/> pathway C3 convertase",
            "Cleavage of Arg-|-Ala bond in complement component C4 to form C4a and C4b, and Lys(or Arg)-|-Lys bond in complement component C2 to form C2a and C2b: the 'classical' pathway C3 convertase"));
      dataHolders.add( new DataHolder("3.4.23.18","Generally favours hydrophobic residues in P1 and P1', but also accepts Lys in P1, which leads to activation of trypsinogen",
            "Generally favors hydrophobic residues in P1 and P1', but also accepts Lys in P1, which leads to activation of trypsinogen"));
      // RA 2005-08-09
      dataHolders.add( new DataHolder("3.4.24.52",
             "Cleavage of only two bonds: 10-His<scissile/>Leu-11 and 14-Ala<scissile/>Leu-15, in the insulin B chain.",
             "Cleavage of only two bonds: 10-His-|-Leu-11 and 14-Ala-|-Leu-15, in the insulin B chain."));
      dataHolders.add(new DataHolder("3.5.4.30", "dCTP + <b>2</b> H<smallsub>2</smallsub>O = dUMP + diphosphate + NH<smallsub>3</smallsub>",
       "dCTP + 2 H(2)O = dUMP + diphosphate + NH(3)"));
      dataHolders.add( new DataHolder("3.6.3.8",
            "ATP + H<smallsub>2</smallsub>O + Ca<smallsup>2+</smallsup><smallsub><stereo>cis</stereo></smallsub> = ADP + phosphate + Ca<smallsup>2+</smallsup><smallsub><stereo>trans</stereo></smallsub>",
            "ATP + H(2)O + Ca(2+)(Cis) = ADP + phosphate + Ca(2+)(Trans)"));
      dataHolders.add(new DataHolder("4.1.1.2",
              "oxalate + H<smallsup>+</smallsup> = formate + CO<smallsub>2</smallsub>",
              "Oxalate = formate + CO(2)"));
      dataHolders.add(new DataHolder("4.1.1.4",
              "acetoacetate + H<smallsup>+</smallsup> = acetone + CO<smallsub>2</smallsub>",
              "Acetoacetate = acetone + CO(2)"));
/*
      dataHolders.add(new DataHolder("4.1.1.39",
              "<b>2</b> 3-phospho-<stereo>D</stereo>-glycerate + <b>2</b> H<smallsup>+</smallsup> = <stereo>D</stereo>-ribulose 1,5-bisphosphate + CO<smallsub>2</smallsub> + H<smallsub>2</smallsub>O",
              "2 3-phospho-D-glycerate = D-ribulose 1,5-bisphosphate + CO(2) + H(2)O"));
*/
    dataHolders.add(new DataHolder("4.1.1.83",
              "(4-hydroxyphenyl)acetate + H<smallsup>+</smallsup> = 4-methylphenol + CO<smallsub>2</smallsub>",
              "(4-hydroxyphenyl)acetate = 4-methylphenol + CO(2)"));
      dataHolders.add(new DataHolder("4.1.1.85",
              "3-dehydro-<stereo>L</stereo>-gulonate 6-phosphate + H<smallsup>+</smallsup> = <stereo>L</stereo>-xylulose 5-phosphate + CO<smallsub>2</smallsub>",
              "3-dehydro-L-gulonate 6-phosphate = L-xylulose 5-phosphate + CO(2)"));
      dataHolders.add(new DataHolder("4.2.1.106",
        "7<stereo>alpha</stereo>,12<stereo>alpha</stereo>-dihydroxy-3-oxochol-4-enoate = 12<stereo>alpha</stereo>-hydroxy-3-oxochola-4,6-dienoate + H<smallsub>2</smallsub>O",
        "7-alpha,12-alpha-dihydroxy-3-oxochol-4-enoate = 12-alpha-hydroxy-3-oxochola-4,6-dienoate + H(2)O"));
      dataHolders.add(new DataHolder("6.2.1.23",
    		  "ATP + an <greek>alpha</greek><greek>omega</greek>-dicarboxylic acid = AMP + diphosphate + an <locant>omega</locant>-carboxyacyl-CoA",
    		  "ATP + an alpha-omega-dicarboxylic acid = AMP + diphosphate + an omega-carboxyacyl-CoA"));

      dataHolders.add(new DataHolder("unknown",
    		  "[tRNA]-guanine + queuine = [tRNA]-queuine + guanine",
    		  "[tRNA]-guanine + queuine = [tRNA]-queuine + guanine"));
   }

   public CALineTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

   /**
    * Tests all the reactions provided in the reaction map.
    */
   public void testData(){
      Iterator iter = dataHolders.iterator();
      while ( iter.hasNext() ) {
         DataHolder reactionData = (DataHolder) iter.next();
         String translated = translator.toASCII(reactionData.getIntenzData(), true, false);
         String translatedString = sc.xml2Display( translated, EncodingType.SWISSPROT_CODE);
         if( !translatedString.equals(reactionData.getEnzymeData()) ){
            LOGGER.error("ASSERT FALSE - EC "+reactionData.getEC()+"  ORIGINAL IntEnz XML \n"+reactionData.getIntenzData()+"\n"+translatedString+" SHOULD BE \n"+reactionData.getEnzymeData());
            assertFalse(true);
         }
      }
   }
}
