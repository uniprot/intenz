package uk.ac.ebi.intenz.tools.sib.writer;

import junit.framework.TestCase;

/**
 * This test ...
 *
 * @author Michael Darsow
 * @version 0.1 - 19-Dec-2003
 */
public class TestInsertLineBreaks extends TestCase {

  public TestInsertLineBreaks(String test) {
    super(test);
  }

  public void testIDLine_EC_ok() throws Exception {
    String idLine = "1.1.1.1";
    String result = EnzymeFlatFileWriter.insertLineBreaks(idLine, LineType.ID);
    assertEquals("ID   " + idLine, result);
  }

  public void testIDLine_no_EC() throws Exception {
    String idLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(idLine, LineType.ID);
    assertEquals("ID   " + idLine, result);
  }

  public void testDELine_Name_ok() throws Exception {
    String deLine = "Beta-fructofuranosidase.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(deLine, LineType.DE);
    assertEquals("DE   " + deLine, result);
  }

  public void testDELine_no_Name() throws Exception {
    String deLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(deLine, LineType.DE);
    assertEquals("DE   " + deLine, result);
  }

  public void testDELine_transferred_entry() throws Exception {
    String deLine = "Transferred entry: 3.2.1.52.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(deLine, LineType.DE);
    assertEquals("DE   " + deLine, result);
  }

  public void testDELine_deleted_entry() throws Exception {
    String deLine = "Deleted entry.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(deLine, LineType.DE);
    assertEquals("DE   " + deLine, result);
  }

  public void testANLine_Name_ok() throws Exception {
    String anLine = "L-glutamine amidohydrolase.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(anLine, LineType.AN);
    assertEquals("AN   " + anLine, result);
  }

  public void testANLine_no_Name() throws Exception {
    String anLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(anLine, LineType.AN);
    assertEquals("AN   " + anLine, result);
  }

  public void testAN_1() throws Exception {
    String anLine = "Nitrate reductase (reduced nicotinamide adenine dinucleotide (phosphate)).";
    String expected = "AN   Nitrate reductase (reduced nicotinamide adenine dinucleotide\n" +
            "AN   (phosphate)).";
    String result = EnzymeFlatFileWriter.insertLineBreaks(anLine, LineType.AN);
    assertEquals(expected, result);
  }

  public void testAN_2() throws Exception {
    String anLine = "H(2)-forming N(5),N(10)-methylenetetrahydromethanopterin dehydrogenase.";
    String expected = "AN   H(2)-forming N(5),N(10)-methylenetetrahydromethanopterin dehydrogenase.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(anLine, LineType.AN);
    assertEquals(expected, result);
  }

  public void testAN_3() throws Exception {
    String anLine = "UDP-Gal:Gal-beta-1->4GlcNAc-R alpha-1->3-galactosyltransferase.";
    String expected = "AN   UDP-Gal:Gal-beta-1->4GlcNAc-R alpha-1->3-galactosyltransferase.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(anLine, LineType.AN);
    assertEquals(expected, result);
  }

  public void testCALine_reaction_ok() throws Exception {
    String caLine = "Sn-glycerol 3-phosphate + NAD(+) = glycerone phosphate + NADH.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals("CA   " + caLine, result);
  }

  public void testCALine_no_reaction() throws Exception {
    String caLine = "Deleted entry.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals("CA   " + caLine, result);
  }

  public void testCALine_equalSign() throws Exception {
    String caLine = "UDP-N-acetyl-D-glucosamine + 3-(alpha-D-mannosyl)-beta-D-mannosyl-R = UDP +" +
            " 3-(2-[N-acetyl-beta-D-glucosaminyl]-alpha-D-mannosyl)-beta-D-mannosyl-R";
    String caLineExpected = "CA   UDP-N-acetyl-D-glucosamine + 3-(alpha-D-mannosyl)-beta-D-mannosyl-R =\n" +
            "CA   UDP + 3-(2-[N-acetyl-beta-D-glucosaminyl]-alpha-D-mannosyl)-beta-D-\n" +
            "CA   mannosyl-R.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals(caLineExpected, result);
  }

  public void testCALine_wrapped_in_name() throws Exception {
    String caLine = "3-carboxy-2-hydroxy-4-methylpentanoate + NAD(+) = 3-carboxy-4-methyl-2-oxopentanoate + NADH";
    String caLineExpected = "CA   3-carboxy-2-hydroxy-4-methylpentanoate + NAD(+) = 3-carboxy-4-methyl-2-\nCA   oxopentanoate + NADH.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals(caLineExpected, result);
  }

  public void testCALine_1() throws Exception {
    String caLine = "GDP-L-fucose + N(4)-{N-acetyl-beta-D-glucosaminyl-(1->2)-alpha-D-mannosyl-(1->3)-" +
            "[N-acetyl-beta-D-glucosaminyl-(1->2)-alpha-D-mannosyl-(1->6)]-beta-D-mannosyl-(1->4)-N-acetyl-" +
            "beta-D-glucosaminyl-(1->4)-N-acetyl-beta-D-glucosaminyl}asparagine = GDP + N(4)-{N-acetyl-beta-D-" +
            "glucosaminyl-(1->2)-alpha-D-mannosyl-(1->3)-[N-acetyl-beta-D-glucosaminyl-(1->2)-alpha-D-mannosyl-(1->6)]" +
            "-beta-D-mannosyl-(1->4)-N-acetyl-beta-D-glucosaminyl-(1->4)-[alpha-L-fucosyl-(1->6)]-N-acetyl-" +
            "beta-D-glucosaminyl}asparagine.";
    String caLineExpected = "CA   GDP-L-fucose + N(4)-{N-acetyl-beta-D-glucosaminyl-(1->2)-alpha-D-\n" +
            "CA   mannosyl-(1->3)-[N-acetyl-beta-D-glucosaminyl-(1->2)-alpha-D-mannosyl-\n" +
            "CA   (1->6)]-beta-D-mannosyl-(1->4)-N-acetyl-beta-D-glucosaminyl-(1->4)-N-\n" +
            "CA   acetyl-beta-D-glucosaminyl}asparagine = GDP + N(4)-{N-acetyl-beta-D-\n" +
            "CA   glucosaminyl-(1->2)-alpha-D-mannosyl-(1->3)-[N-acetyl-beta-D-\n" +
            "CA   glucosaminyl-(1->2)-alpha-D-mannosyl-(1->6)]-beta-D-mannosyl-(1->4)-N-\n" +
            "CA   acetyl-beta-D-glucosaminyl-(1->4)-[alpha-L-fucosyl-(1->6)]-N-acetyl-beta-\n" +
            "CA   D-glucosaminyl}asparagine.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals(caLineExpected, result);
  }

  public void testCALine_2() throws Exception {
    String caLine = "L-ascorbate + ferricytochrome b5 = monodehydroascorbate + ferrocytochrome b5.";
    String caLineExpected = "CA   L-ascorbate + ferricytochrome b5 = monodehydroascorbate +\n" +
            "CA   ferrocytochrome b5.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals(caLineExpected, result);
  }

  public void testCALine_3() throws Exception {
    String caLine = "Dolichyl diphosphooligosaccharide + protein L-asparagine = dolichyl diphosphate + a " +
            "glycoprotein with the oligosaccharide chain attached by glycosylamine linkage to protein L-asparagine.";
    String caLineExpected = "CA   Dolichyl diphosphooligosaccharide + protein L-asparagine = dolichyl\n" +
            "CA   diphosphate + a glycoprotein with the oligosaccharide chain attached by\n" +
            "CA   glycosylamine linkage to protein L-asparagine.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
    assertEquals(caLineExpected, result);
  }

  public void testCFLine_cofactors_ok() throws Exception {
    String cfLine = "Manganese; Monovalent cation.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(cfLine, LineType.CF);
    assertEquals("CF   " + cfLine, result);
  }

  public void testCFLine_no_cofactors() throws Exception {
    String cfLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(cfLine, LineType.CF);
    assertEquals("CF   " + cfLine, result);
  }

  public void testCCLine_comment_ok() throws Exception {
    String ccLine = "Acts on primary or secondary alcohols or hemiacetals. The animal, but not the yeast, enzyme " +
            "acts also on cyclic secondary alcohols.";
    String ccLineExpected = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\nCC   -!- " +
            "The animal, but not the yeast, enzyme acts also on cyclic secondary\nCC       alcohols.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_no_comment() throws Exception {
    String ccLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);
    assertEquals("CC   -!- " + ccLine + "\n", result);
  }

  public void testCCLine_comment_2() throws Exception {
    String ccLine = "Some members of this group oxidize only primary alcohols; others act also on secondary alcohols." +
            " May be identical with EC 1.1.1.19, EC 1.1.1.33 and EC 1.1.1.55. A-specific with respect to NADPH.";
    String ccLineExpected = "CC   -!- Some members of this group oxidize only primary alcohols; others act\n" +
            "CC       also on secondary alcohols.\nCC   -!- May be identical with EC 1.1.1.19, EC 1.1.1.33 and " +
            "EC 1.1.1.55.\nCC   -!- A-specific with respect to NADPH.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_3() throws Exception {
    String ccLine = "Acts on prostaglandins E2, F2-alpha and B1, but not on prostaglandin D2 (cf. EC 1.1.1.141 and EC 1.1.1.196)." +
            " May be identical with EC 1.1.1.189.";
    String ccLineExpected = "CC   -!- Acts on prostaglandins E2, F2-alpha and B1, but not on prostaglandin\n" +
            "CC       D2 (cf. EC 1.1.1.141 and EC 1.1.1.196).\nCC   -!- May be identical with EC 1.1.1.189.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_4() throws Exception {
    String ccLine = "Acts on a wide range of carbonyl compounds, including quinones, aromatic aldehydes, ketoaldehydes," +
            " daunorubicin, and prostaglandins" +
            " E and F, reducing them to the corresponding alcohol. B-specific with respect to NADPH (cf. EC 1.1.1.2).";
    String ccLineExpected = "CC   -!- Acts on a wide range of carbonyl compounds, including quinones,\nCC" +
            "       aromatic aldehydes, ketoaldehydes, daunorubicin, and prostaglandins\nCC       E and F," +
            " reducing them to the corresponding alcohol.\nCC   -!- B-specific with respect to NADPH (cf. EC 1.1.1.2).\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

//  public void testCCLine_comment_5() throws Exception {
//    String ccLine = "Studies on the effects of growth-stage and nutrient supply on the stereochemistry " +
//            "of sulcatone reduction in Clostridia pasteurianum," +
//            " C. tyrobutyricum and Lactobacillus brevis suggest that there may be at least two sulcatone " +
//            "reductases with different stereospecificities.";
//    String ccLineExpected = "CC   -!- Studies on the effects of growth-stage and nutrient supply on the\n" +
//            "CC       stereochemistry of sulcatone reduction in Clostridia pasteurianum,\n" +
//            "CC       C. tyrobutyricum and Lactobacillus brevis suggest that there may be\n" +
//            "CC       at least two sulcatone reductases with different stereospecificities.\n";
//    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);
//
//    assertSame(ccLineExpected, result);
//  }

  public void testCCLine_comment_6() throws Exception {
    String ccLine = "Similar to transcription elongation factor B (SIII), polypeptide 1-like{EI1}";
    String ccLineExpected = "CC   -!- Similar to transcription elongation factor B (SIII), polypeptide\nCC       1-like{EI1}\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_7() throws Exception {
    String ccLine = "Similar to transcription elongation factor B (SIII), polypeptide EC 1.1.1.1";
    String ccLineExpected = "CC   -!- Similar to transcription elongation factor B (SIII), polypeptide\nCC       EC 1.1.1.1\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_8() throws Exception {
    String ccLine = "Also acts on D-glucitol (giving D-fructose) and other closely related sugar alcohols.";
    String ccLineExpected = "CC   -!- Also acts on D-glucitol (giving D-fructose) and other closely related\nCC       sugar alcohols.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_9() throws Exception {
    String ccLine = "The isomer of isocitrate involved is (1R,2S)-1-hydroxypropane-1,2,3-tricarboxylate, formerly " +
            "termed threo-D(S)-isocitrate. Does not decarboxylate added oxalosuccinate.";
    String ccLineExpected = "CC   -!- The isomer of isocitrate involved is (1R,2S)-1-hydroxypropane-1,2,3-\n" +
            "CC       tricarboxylate, formerly termed threo-D(S)-isocitrate.\nCC   -!- Does not decarboxylate added" +
            " oxalosuccinate.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_10() throws Exception {
    String ccLine = "Chlordecone, an organochlorine pesticide, is 1,1A,3,3A,4,5,5A,5B,6-" +
            "decachlorooctahydro-1,3,4-metheno-2H-cyclobuta[CD]pentalen-2-one.";
    String ccLineExpected = "CC   -!- Chlordecone, an organochlorine pesticide, is 1,1A,3,3A,4,5,5A,5B,6-\n" +
            "CC       decachlorooctahydro-1,3,4-metheno-2H-cyclobuta[CD]pentalen-2-one.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_11() throws Exception {
//CC   -!- Depending on the organism and physiological conditions, either two or
//CC       four protons are extruded from the cytoplasmic to the non-cytoplasmic
//CC       compartment (cf. EC 1.6.99.3).
    String ccLine = "Depending on the organism and physiological conditions, either two or four protons " +
            "are extruded from the cytoplasmic to the non-cytoplasmic compartment (cf. EC 1.6.99.3).";
    String ccLineExpected = "CC   -!- Depending on the organism and physiological conditions, either two or\n" +
            "CC       four protons are extruded from the cytoplasmic to the non-cytoplasmic\n" +
            "CC       compartment (cf. EC 1.6.99.3).\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_12() throws Exception {
//CC   -!- The product (9Z,12Z)-(11S)-11-hydroperoxyoctadeca-9,12-dienoate, is
//CC       converted, more slowly, into (9Z,11E)-(13R)-13-hydroperoxyoctadeca-
//CC       9,11-dienoate.
    String ccLine = "The product (9Z,12Z)-(11S)-11-hydroperoxyoctadeca-9,12-dienoate, is converted, more " +
            "slowly, into (9Z,11E)-(13R)-13-hydroperoxyoctadeca-9,11-dienoate.";
    String ccLineExpected = "CC   -!- The product (9Z,12Z)-(11S)-11-hydroperoxyoctadeca-9,12-dienoate,\n" +
            "CC       is converted, more slowly, into (9Z,11E)-(13R)-13-\n" +
            "CC       hydroperoxyoctadeca-9,11-dienoate.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_13() throws Exception {
    String ccLine = "Some other aromatic compounds, including ethylbenzene, 4-xylene and some halogenated " +
            "toluenes, are converted into the corresponding cis-dihydrodiols.";
    String ccLineExpected = "CC   -!- Some other aromatic compounds, including ethylbenzene, 4-xylene and\n" +
            "CC       some halogenated toluenes, are converted into the corresponding cis-\n" +
            "CC       dihydrodiols.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_14() throws Exception {
    String ccLine = "Acts on the 3-alpha-hydroxyl group of androgens of the 5-alpha-androstane series; and also, " +
            "more slowly, on the 17-alpha-hydroxyl group of both androgenic and estrogenic substrates (cf. EC 1.1.1.51).";
    String ccLineExpected = "CC   -!- Acts on the 3-alpha-hydroxyl group of androgens of the 5-alpha-\n" +
            "CC       androstane series; and also, more slowly, on the 17-alpha-hydroxyl\n" +
            "CC       group of both androgenic and estrogenic substrates (cf. EC 1.1.1.51).\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

  public void testCCLine_comment_15() throws Exception {
    String ccLine = "Trypanothione is the oxidized form of N(1),N(6)-bis(glutathionyl) spermidine from the " +
            "insect-parasitic trypanosomatid Crithidia fasciculata.";
    String ccLineExpected = "CC   -!- Trypanothione is the oxidized form of N(1),N(6)-bis(glutathionyl)\n" +
            "CC       spermidine from the insect-parasitic trypanosomatid Crithidia\n" +
            "CC       fasciculata.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }

/*
 * Removed, as the last line does not indent more than '(2)' currently
  public void testCCLine_comment_16() throws Exception {
    String ccLine = "The overall reaction takes place in two separate steps: (1) 2 inosine + O(2) = 2 5'-" +
            "dehydroinosine + 2 H(2)O. (2) 2 5'-dehydroinosine + O(2) = 2 9-riburonosylhypoxanthine + 2 H(2)O.";
    String ccLineExpected = "CC   -!- The overall reaction takes place in two separate steps:\n" +
            "CC       (1) 2 inosine + O(2) = 2 5'-dehydroinosine + 2 H(2)O.\n" +
            "CC       (2) 2 5'-dehydroinosine + O(2) = 2 9-riburonosylhypoxanthine +\n" +
            "CC           2 H(2)O.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }
*/
/*
 * Removed test, as it doesn't exist in enzyme.dat anymore
  public void testCCLine_comment_17() throws Exception {
    String ccLine = "Catalyzes three types of monooxygenase (Baeyer-Villiger oxidation) reaction. (1) The oxidative " +
            "esterification of a number of derivatives of pregn-4-ene-3,20-dione (progesterone) to produce the " +
            "corresponding 17-alpha-hydroxysteroid 17-acetate ester, such as testosterone acetate: progesterone + " +
            "NADPH + O(2) = testosterone acetate + NADP(+) + H(2)O. (2) The oxidative lactonization of a number of" +
            " derivatives of androst-4-ene-3,17-dione (androstenedione) to produce the 13,17-" +
            "secoandrosteno-17,13-alpha-lactone, such as 3-oxo-13,17-secoandrost-4-eno-17,13-alpha-lactone (testololactone):" +
            " androstenedione + NADPH + O(2) = testololactone + NADP(+) + H(2)O. (3) The oxidative cleavage of the " +
            "17beta-side-chain of 17-alpha-hydroxypregn-4-ene-3,20-dione (17-alpha-hydroxyprogesterone) to" +
            " produce androst-4-ene-3,17-dione (androstenedione) and acetate: 17-alpha-hydroxyprogesterone + NADPH + " +
            "O(2) = androstenedione + acetate + NADP(+) + H(2)O. Reaction 1 is also catalyzed by EC 1.14.99.4 and " +
            "reactions 2 and 3 correspond to that catalyzed by EC 1.14.99.12. The possibility that a single enzyme " +
            "is responsible for the reactions ascribed to EC 1.14.99.4 and EC 1.14.99.12 in other tissues cannot be excluded.";
    String ccLineExpected = "CC   -!- Catalyzes three types of monooxygenase (Baeyer-Villiger oxidation)\n" +
            "CC       reaction.\n" +
            "CC   -!- (1) The oxidative esterification of a number of derivatives of pregn-\n" +
            "CC       4-ene-3,20-dione (progesterone) to produce the corresponding 17-\n" +
            "CC       alpha-hydroxysteroid 17-acetate ester, such as testosterone acetate:\n" +
            "CC       progesterone + NADPH + O(2) = testosterone acetate + NADP(+) + H(2)O.\n" +
            "CC   -!- (2) The oxidative lactonization of a number of derivatives of\n" +
            "CC       androst-4-ene-3,17-dione (androstenedione) to produce the 13,17-\n" +
            "CC       secoandrosteno-17,13-alpha-lactone, such as 3-oxo-13,17-secoandrost-\n" +
            "CC       4-eno-17,13-alpha-lactone (testololactone): androstenedione + NADPH +\n" +
            "CC       O(2) = testololactone + NADP(+) + H(2)O.\n" +
            "CC   -!- (3) The oxidative cleavage of the 17beta-side-chain of 17-alpha-\n" +
            "CC       hydroxypregn-4-ene-3,20-dione (17-alpha-hydroxyprogesterone) to\n" +
            "CC       produce androst-4-ene-3,17-dione (androstenedione) and acetate: 17-\n" +
            "CC       alpha-hydroxyprogesterone + NADPH + O(2) = androstenedione + acetate\n" +
            "CC       + NADP(+) + H(2)O.\n" +
            "CC   -!- Reaction 1 is also catalyzed by EC 1.14.99.4 and reactions 2 and 3\n" +
            "CC       correspond to that catalyzed by EC 1.14.99.12.\n" +
            "CC   -!- The possibility that a single enzyme is responsible for the reactions\n" +
            "CC       ascribed to EC 1.14.99.4 and EC 1.14.99.12 in other tissues cannot be\n" +
            "CC       excluded.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }
*/
/*
 * Removed, as 'a second molecule...' does not indent more than '(2)' currently
  public void testCCLine_comment_18() throws Exception {
    String ccLine = "The reaction of this enzyme occurs in three steps: (1) NAD-dependent dehydrogenation of putrescine. " +
            "(2) Transfer of the 4-aminobutylidene group from dehydroputrescine to a second molecule of putrescine. " +
            "(3) Reduction of the imine intermediate to form homospermidine." +
            " Hence the overall reaction is transfer of a 4-aminobutyl group.";
    String ccLineExpected = "CC   -!- The reaction of this enzyme occurs in three steps:\n" +
            "CC       (1) NAD-dependent dehydrogenation of putrescine.\n" +
            "CC       (2) Transfer of the 4-aminobutylidene group from dehydroputrescine to\n" +
            "CC           a second molecule of putrescine.\n" +
            "CC       (3) Reduction of the imine intermediate to form homospermidine.\n" +
            "CC   -!- Hence the overall reaction is transfer of a 4-aminobutyl group.\n";
    String result = EnzymeFlatFileWriter.insertLineBreaks(ccLine, LineType.CC);

    assertEquals(ccLineExpected, result);
  }
*/
  public void testDILine_mimLink_ok() throws Exception {
    String diLine = "Sorbitol dehydrogenase deficiency; MIM:182500";
    String diLineExpected = "DI   Sorbitol dehydrogenase deficiency; MIM:182500.";
    String result = EnzymeFlatFileWriter.insertLineBreaks(diLine, LineType.DI);

    assertEquals(diLineExpected, result);
  }

  public void testDILine_no_mimLink() throws Exception {
    String diLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(diLine, LineType.DI);

    assertEquals("DI   " + diLine, result);
  }

  public void testPRLine_prositeLink_ok() throws Exception {
    String prLine = "PROSITE; PDOC00058";
    String prLineExpected = "PR   PROSITE; PDOC00058";
    String result = EnzymeFlatFileWriter.insertLineBreaks(prLine, LineType.PR);

    assertEquals(prLineExpected, result);
  }

  public void testPRLine_no_prositeLink() throws Exception {
    String prLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(prLine, LineType.PR);

    assertEquals("PR   " + prLine, result);
  }

  public void testDRLine_spLink_ok() throws Exception {
    String spLine = "Q9Z9U1, DHSO_BACHD;  Q06004, DHSO_BACSU;  Q02912, DHSO_BOMMO";
    String spLineExpected = "DR   Q9Z9U1, DHSO_BACHD;  Q06004, DHSO_BACSU;  Q02912, DHSO_BOMMO";
    String result = EnzymeFlatFileWriter.insertLineBreaks(spLine, LineType.DR);

    assertEquals(spLineExpected, result);
  }

  public void testDRLine_no_spLink() throws Exception {
    String spLine = "";
    String result = EnzymeFlatFileWriter.insertLineBreaks(spLine, LineType.DR);

    assertEquals("DR   " + spLine, result);
  }
  
  public void testCALineDashParenthesis() throws Exception {
	  String expected =
		  "CA   Hydrolysis of (1->4)-beta-D-glucosidic links in oligoxyloglucans so as to\n" +
		  "CA   remove successive isoprimeverose (i.e. alpha-xylo-(1->6)-beta-D-\n" + // w/o 'glucosyl-'
		  "CA   glucosyl-) residues from the non-reducing chain ends.";
	  String caLine = "Hydrolysis of (1->4)-beta-D-glucosidic links in oligoxyloglucans so" +
	  		" as to remove successive isoprimeverose (i.e." +
	  		" alpha-xylo-(1->6)-beta-D-glucosyl-) residues from the non-reducing chain ends.";
	  String result = EnzymeFlatFileWriter.insertLineBreaks(caLine, LineType.CA);
	  assertEquals(expected, result);
  }
}