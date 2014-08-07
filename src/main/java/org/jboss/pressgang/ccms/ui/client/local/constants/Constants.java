/*
 * Copyright 2011-2014 Red Hat, Inc.
 *
 * This file is part of PressGang CCMS.
 *
 * PressGang CCMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PressGang CCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PressGang CCMS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jboss.pressgang.ccms.ui.client.local.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.core.client.GWT;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeTypeV1;
import org.jetbrains.annotations.NotNull;

/**
 * This class contains a number of constants used throughout the application.
 *
 * @author Matthew Casperson
 */
public final class Constants {
    /**
     * The UI version
     */
    public static final String VERSION = "1.9-SNAPSHOT";

    /**
     * The UI Build - yyyymmddhhmm.
     */
    public static final String BUILD = "201408071455";

    public static final String DUMMY_CS_ENTITIES = "<!ENTITY PRODUCT \"Product\">\n" +
            "<!ENTITY BOOKID \"BOOKID\">\n" +
            "<!ENTITY YEAR \"YYYY\">\n" +
            "<!ENTITY TITLE \"TITLE\">\n" +
            "<!ENTITY HOLDER \"HOLDER\">\n" +
            "<!ENTITY BZURL \"BZURL\">\n" +
            "<!ENTITY BZCOMPONENT \"BZCOMPONENT\">\n" +
            "<!ENTITY BZPRODUCT \"BZPRODUCT\">\n";

    public static final List<String> DUMMY_CS_ENTITY_NAMES = Arrays.asList("PRODUCT", "BOOKID", "YEAR", "TITLE", "HOLDER", "BZURL",
            "BZCOMPONENT", "BZPRODUCT");

    public static final String DEFAULT_BUGZILLA_URL = "https://bugzilla.redhat.com/";
    public static final String DEFAULT_BZCOMPONENT = "documentation";

    public static final List<String> AVAILABLE_LOCALES = Arrays.asList("ar",
            "ar-AE",
            "ar-BH",
            "ar-DZ",
            "ar-EG",
            "ar-IQ",
            "ar-JO",
            "ar-KW",
            "ar-LB",
            "ar-LY",
            "ar-MA",
            "ar-OM",
            "ar-QA",
            "ar-SA",
            "ar-SD",
            "ar-SY",
            "ar-TN",
            "ar-YE",
            "be",
            "be-BY",
            "bg",
            "bg-BG",
            "ca",
            "ca-ES",
            "cs",
            "cs-CZ",
            "da",
            "da-DK",
            "de",
            "de-AT",
            "de-CH",
            "de-DE",
            "de-LU",
            "el",
            "el-CY",
            "el-GR",
            "en",
            "en-AU",
            "en-CA",
            "en-GB",
            "en-IE",
            "en-IN",
            "en-MT",
            "en-NZ",
            "en-PH",
            "en-SG",
            "en-US",
            "en-ZA",
            "es",
            "es-AR",
            "es-BO",
            "es-CL",
            "es-CO",
            "es-CR",
            "es-DO",
            "es-EC",
            "es-ES",
            "es-GT",
            "es-HN",
            "es-MX",
            "es-NI",
            "es-PA",
            "es-PE",
            "es-PR",
            "es-PY",
            "es-SV",
            "es-US",
            "es-UY",
            "es-VE",
            "et",
            "et-EE",
            "fi",
            "fi-FI",
            "fr",
            "fr-BE",
            "fr-CA",
            "fr-CH",
            "fr-FR",
            "fr-LU",
            "ga",
            "ga-IE",
            "hi-IN",
            "hr",
            "hr-HR",
            "hu",
            "hu-HU",
            "in",
            "in-ID",
            "is",
            "is-IS",
            "it",
            "it-CH",
            "it-IT",
            "iw",
            "iw-IL",
            "ja",
            "ja-JP",
            "ko",
            "ko-KR",
            "lt",
            "lt-LT",
            "lv",
            "lv-LV",
            "mk",
            "mk-MK",
            "ms",
            "ms-MY",
            "mt",
            "mt-MT",
            "nl",
            "nl-BE",
            "nl-NL",
            "no",
            "no-NO",
            "no-NO-NY",
            "pl",
            "pl-PL",
            "pt",
            "pt-BR",
            "pt-PT",
            "ro",
            "ro-RO",
            "ru",
            "ru-RU",
            "sk",
            "sk-SK",
            "sl",
            "sl-SI",
            "sq",
            "sq-AL",
            "sr",
            "sr-BA",
            "sr-CS",
            "sr-ME",
            "sr-RS",
            "sv",
            "sv-SE",
            "th",
            "th-TH",
            "tr",
            "tr-TR",
            "uk",
            "uk-UA",
            "vi",
            "vi-VN",
            "zh",
            "zh-CN",
            "zh-HK",
            "zh-SG",
            "zh-TW");

    /**
     * A collection of all the Docbook 4.5 entities
     */
    public static final List<String> DOCBOOK_45_ENTITIES = new ArrayList<String>() {{
        add("euro");
        add("cularr");
        add("curarr");
        add("dArr");
        add("darr2");
        add("dharl");
        add("dharr");
        add("dlarr");
        add("drarr");
        add("hArr");
        add("harr");
        add("harrw");
        add("lAarr");
        add("Larr");
        add("larr2");
        add("larrhk");
        add("larrlp");
        add("larrtl");
        add("lhard");
        add("lharu");
        add("lrarr2");
        add("lrhar2");
        add("lsh");
        add("map");
        add("mumap");
        add("nearr");
        add("nhArr");
        add("nharr");
        add("nlArr");
        add("nlarr");
        add("nrArr");
        add("nrarr");
        add("nwarr");
        add("olarr");
        add("orarr");
        add("rAarr");
        add("Rarr");
        add("rarr2");
        add("rarrhk");
        add("rarrlp");
        add("rarrtl");
        add("rarrw");
        add("rhard");
        add("rharu");
        add("rlarr2");
        add("rlhar2");
        add("rsh");
        add("uArr");
        add("uarr2");
        add("uharl");
        add("uharr");
        add("vArr");
        add("varr");
        add("xhArr");
        add("xharr");
        add("xlArr");
        add("xrArr");
        add("amalg");
        add("Barwed");
        add("barwed");
        add("Cap");
        add("coprod");
        add("Cup");
        add("cuvee");
        add("cuwed");
        add("diam");
        add("divonx");
        add("intcal");
        add("lthree");
        add("ltimes");
        add("minusb");
        add("oast");
        add("ocir");
        add("odash");
        add("odot");
        add("ominus");
        add("oplus");
        add("osol");
        add("otimes");
        add("plusb");
        add("plusdo");
        add("prod");
        add("rthree");
        add("rtimes");
        add("sdot");
        add("sdotb");
        add("setmn");
        add("sqcap");
        add("sqcup");
        add("ssetmn");
        add("sstarf");
        add("sum");
        add("timesb");
        add("top");
        add("uplus");
        add("wreath");
        add("xcirc");
        add("xdtri");
        add("xutri");
        add("dlcorn");
        add("drcorn");
        add("lceil");
        add("lfloor");
        add("lpargt");
        add("rceil");
        add("rfloor");
        add("rpargt");
        add("ulcorn");
        add("urcorn");
        add("gnap");
        add("gnE");
        add("gne");
        add("gnsim");
        add("gvnE");
        add("lnap");
        add("lnE");
        add("lne");
        add("lnsim");
        add("lvnE");
        add("nap");
        add("ncong");
        add("nequiv");
        add("ngE");
        add("nge");
        add("nges");
        add("ngt");
        add("nlE");
        add("nle");
        add("nles");
        add("nlt");
        add("nltri");
        add("nltrie");
        add("nmid");
        add("npar");
        add("npr");
        add("npre");
        add("nrtri");
        add("nrtrie");
        add("nsc");
        add("nsce");
        add("nsim");
        add("nsime");
        add("nsmid");
        add("nspar");
        add("nsub");
        add("nsubE");
        add("nsube");
        add("nsup");
        add("nsupE");
        add("nsupe");
        add("nVDash");
        add("nVdash");
        add("nvDash");
        add("nvdash");
        add("prnap");
        add("prnE");
        add("prnsim");
        add("scnap");
        add("scnE");
        add("scnsim");
        add("subnE");
        add("subne");
        add("supnE");
        add("supne");
        add("vsubnE");
        add("vsubne");
        add("vsupnE");
        add("vsupne");
        add("ang");
        add("angmsd");
        add("beth");
        add("bprime");
        add("comp");
        add("daleth");
        add("ell");
        add("empty");
        add("gimel");
        add("inodot");
        add("jnodot");
        add("nexist");
        add("oS");
        add("planck");
        add("real");
        add("sbsol");
        add("vprime");
        add("weierp");
        add("ape");
        add("asymp");
        add("bcong");
        add("bepsi");
        add("bowtie");
        add("bsim");
        add("bsime");
        add("bump");
        add("bumpe");
        add("cire");
        add("colone");
        add("cuepr");
        add("cuesc");
        add("cupre");
        add("dashv");
        add("ecir");
        add("ecolon");
        add("eDot");
        add("efDot");
        add("egs");
        add("els");
        add("erDot");
        add("esdot");
        add("fork");
        add("frown");
        add("gap");
        add("gE");
        add("gEl");
        add("gel");
        add("ges");
        add("Gg");
        add("gl");
        add("gsdot");
        add("gsim");
        add("Gt");
        add("lap");
        add("ldot");
        add("lE");
        add("lEg");
        add("leg");
        add("les");
        add("lg");
        add("Ll");
        add("lsim");
        add("Lt");
        add("ltrie");
        add("mid");
        add("models");
        add("pr");
        add("prap");
        add("pre");
        add("prsim");
        add("rtrie");
        add("samalg");
        add("sc");
        add("scap");
        add("sccue");
        add("sce");
        add("scsim");
        add("sfrown");
        add("smid");
        add("smile");
        add("spar");
        add("sqsub");
        add("sqsube");
        add("sqsup");
        add("sqsupe");
        add("ssmile");
        add("Sub");
        add("subE");
        add("Sup");
        add("supE");
        add("thkap");
        add("thksim");
        add("trie");
        add("twixt");
        add("Vdash");
        add("vDash");
        add("vdash");
        add("veebar");
        add("vltri");
        add("vprop");
        add("vrtri");
        add("Vvdash");
        add("boxDL");
        add("boxDl");
        add("boxdL");
        add("boxdl");
        add("boxDR");
        add("boxDr");
        add("boxdR");
        add("boxdr");
        add("boxH");
        add("boxh");
        add("boxHD");
        add("boxHd");
        add("boxhD");
        add("boxhd");
        add("boxHU");
        add("boxHu");
        add("boxhU");
        add("boxhu");
        add("boxUL");
        add("boxUl");
        add("boxuL");
        add("boxul");
        add("boxUR");
        add("boxUr");
        add("boxuR");
        add("boxur");
        add("boxV");
        add("boxv");
        add("boxVH");
        add("boxVh");
        add("boxvH");
        add("boxvh");
        add("boxVL");
        add("boxVl");
        add("boxvL");
        add("boxvl");
        add("boxVR");
        add("boxVr");
        add("boxvR");
        add("boxvr");
        add("Acy");
        add("acy");
        add("Bcy");
        add("bcy");
        add("CHcy");
        add("chcy");
        add("Dcy");
        add("dcy");
        add("Ecy");
        add("ecy");
        add("Fcy");
        add("fcy");
        add("Gcy");
        add("gcy");
        add("HARDcy");
        add("hardcy");
        add("Icy");
        add("icy");
        add("IEcy");
        add("iecy");
        add("IOcy");
        add("iocy");
        add("Jcy");
        add("jcy");
        add("Kcy");
        add("kcy");
        add("KHcy");
        add("khcy");
        add("Lcy");
        add("lcy");
        add("Mcy");
        add("mcy");
        add("Ncy");
        add("ncy");
        add("numero");
        add("Ocy");
        add("ocy");
        add("Pcy");
        add("pcy");
        add("Rcy");
        add("rcy");
        add("Scy");
        add("scy");
        add("SHCHcy");
        add("shchcy");
        add("SHcy");
        add("shcy");
        add("SOFTcy");
        add("softcy");
        add("Tcy");
        add("tcy");
        add("TScy");
        add("tscy");
        add("Ucy");
        add("ucy");
        add("Vcy");
        add("vcy");
        add("YAcy");
        add("yacy");
        add("Ycy");
        add("ycy");
        add("YUcy");
        add("yucy");
        add("Zcy");
        add("zcy");
        add("ZHcy");
        add("zhcy");
        add("DJcy");
        add("djcy");
        add("DScy");
        add("dscy");
        add("DZcy");
        add("dzcy");
        add("GJcy");
        add("gjcy");
        add("Iukcy");
        add("iukcy");
        add("Jsercy");
        add("jsercy");
        add("Jukcy");
        add("jukcy");
        add("KJcy");
        add("kjcy");
        add("LJcy");
        add("ljcy");
        add("NJcy");
        add("njcy");
        add("TSHcy");
        add("tshcy");
        add("Ubrcy");
        add("ubrcy");
        add("YIcy");
        add("yicy");
        add("acute");
        add("breve");
        add("caron");
        add("cedil");
        add("circ");
        add("dblac");
        add("die");
        add("dot");
        add("grave");
        add("macr");
        add("ogon");
        add("ring");
        add("tilde");
        add("uml");
        add("Agr");
        add("agr");
        add("Bgr");
        add("bgr");
        add("Dgr");
        add("dgr");
        add("EEgr");
        add("eegr");
        add("Egr");
        add("egr");
        add("Ggr");
        add("ggr");
        add("Igr");
        add("igr");
        add("Kgr");
        add("kgr");
        add("KHgr");
        add("khgr");
        add("Lgr");
        add("lgr");
        add("Mgr");
        add("mgr");
        add("Ngr");
        add("ngr");
        add("Ogr");
        add("ogr");
        add("OHgr");
        add("ohgr");
        add("Pgr");
        add("pgr");
        add("PHgr");
        add("phgr");
        add("PSgr");
        add("psgr");
        add("Rgr");
        add("rgr");
        add("sfgr");
        add("Sgr");
        add("sgr");
        add("Tgr");
        add("tgr");
        add("THgr");
        add("thgr");
        add("Ugr");
        add("ugr");
        add("Xgr");
        add("xgr");
        add("Zgr");
        add("zgr");
        add("Aacgr");
        add("aacgr");
        add("Eacgr");
        add("eacgr");
        add("EEacgr");
        add("eeacgr");
        add("Iacgr");
        add("iacgr");
        add("idiagr");
        add("Idigr");
        add("idigr");
        add("Oacgr");
        add("oacgr");
        add("OHacgr");
        add("ohacgr");
        add("Uacgr");
        add("uacgr");
        add("udiagr");
        add("Udigr");
        add("udigr");
        add("alpha");
        add("beta");
        add("chi");
        add("Delta");
        add("delta");
        add("epsi");
        add("epsis");
        add("epsiv");
        add("eta");
        add("Gamma");
        add("gamma");
        add("gammad");
        add("iota");
        add("kappa");
        add("kappav");
        add("Lambda");
        add("lambda");
        add("mu");
        add("nu");
        add("Omega");
        add("omega");
        add("Phi");
        add("phis");
        add("phiv");
        add("Pi");
        add("pi");
        add("piv");
        add("Psi");
        add("psi");
        add("rho");
        add("rhov");
        add("Sigma");
        add("sigma");
        add("sigmav");
        add("tau");
        add("Theta");
        add("thetas");
        add("thetav");
        add("Upsi");
        add("upsi");
        add("Xi");
        add("xi");
        add("zeta");
        add("b.alpha");
        add("b.beta");
        add("b.chi");
        add("b.Delta");
        add("b.delta");
        add("b.epsi");
        add("b.epsiv");
        add("b.eta");
        add("b.Gamma");
        add("b.gamma");
        add("b.Gammad");
        add("b.gammad");
        add("b.iota");
        add("b.kappa");
        add("b.kappav");
        add("b.Lambda");
        add("b.lambda");
        add("b.mu");
        add("b.nu");
        add("b.Omega");
        add("b.omega");
        add("b.Phi");
        add("b.phi");
        add("b.phiv");
        add("b.Pi");
        add("b.pi");
        add("b.piv");
        add("b.Psi");
        add("b.psi");
        add("b.rho");
        add("b.rhov");
        add("b.Sigma");
        add("b.sigma");
        add("b.sigmav");
        add("b.tau");
        add("b.Theta");
        add("b.thetas");
        add("b.thetav");
        add("b.Upsi");
        add("b.upsi");
        add("b.Xi");
        add("b.xi");
        add("b.zeta");
        add("Aacute");
        add("aacute");
        add("Acirc");
        add("acirc");
        add("AElig");
        add("aelig");
        add("Agrave");
        add("agrave");
        add("Aring");
        add("aring");
        add("Atilde");
        add("atilde");
        add("Auml");
        add("auml");
        add("Ccedil");
        add("ccedil");
        add("Eacute");
        add("eacute");
        add("Ecirc");
        add("ecirc");
        add("Egrave");
        add("egrave");
        add("ETH");
        add("eth");
        add("Euml");
        add("euml");
        add("Iacute");
        add("iacute");
        add("Icirc");
        add("icirc");
        add("Igrave");
        add("igrave");
        add("Iuml");
        add("iuml");
        add("Ntilde");
        add("ntilde");
        add("Oacute");
        add("oacute");
        add("Ocirc");
        add("ocirc");
        add("Ograve");
        add("ograve");
        add("Oslash");
        add("oslash");
        add("Otilde");
        add("otilde");
        add("Ouml");
        add("ouml");
        add("szlig");
        add("THORN");
        add("thorn");
        add("Uacute");
        add("uacute");
        add("Ucirc");
        add("ucirc");
        add("Ugrave");
        add("ugrave");
        add("Uuml");
        add("uuml");
        add("Yacute");
        add("yacute");
        add("yuml");
        add("Abreve");
        add("abreve");
        add("Amacr");
        add("amacr");
        add("Aogon");
        add("aogon");
        add("Cacute");
        add("cacute");
        add("Ccaron");
        add("ccaron");
        add("Ccirc");
        add("ccirc");
        add("Cdot");
        add("cdot");
        add("Dcaron");
        add("dcaron");
        add("Dstrok");
        add("dstrok");
        add("Ecaron");
        add("ecaron");
        add("Edot");
        add("edot");
        add("Emacr");
        add("emacr");
        add("ENG");
        add("eng");
        add("Eogon");
        add("eogon");
        add("gacute");
        add("Gbreve");
        add("gbreve");
        add("Gcedil");
        add("Gcirc");
        add("gcirc");
        add("Gdot");
        add("gdot");
        add("Hcirc");
        add("hcirc");
        add("Hstrok");
        add("hstrok");
        add("Idot");
        add("IJlig");
        add("ijlig");
        add("Imacr");
        add("imacr");
        add("inodot");
        add("Iogon");
        add("iogon");
        add("Itilde");
        add("itilde");
        add("Jcirc");
        add("jcirc");
        add("Kcedil");
        add("kcedil");
        add("kgreen");
        add("Lacute");
        add("lacute");
        add("Lcaron");
        add("lcaron");
        add("Lcedil");
        add("lcedil");
        add("Lmidot");
        add("lmidot");
        add("Lstrok");
        add("lstrok");
        add("Nacute");
        add("nacute");
        add("napos");
        add("Ncaron");
        add("ncaron");
        add("Ncedil");
        add("ncedil");
        add("Odblac");
        add("odblac");
        add("OElig");
        add("oelig");
        add("Omacr");
        add("omacr");
        add("Racute");
        add("racute");
        add("Rcaron");
        add("rcaron");
        add("Rcedil");
        add("rcedil");
        add("Sacute");
        add("sacute");
        add("Scaron");
        add("scaron");
        add("Scedil");
        add("scedil");
        add("Scirc");
        add("scirc");
        add("Tcaron");
        add("tcaron");
        add("Tcedil");
        add("tcedil");
        add("Tstrok");
        add("tstrok");
        add("Ubreve");
        add("ubreve");
        add("Udblac");
        add("udblac");
        add("Umacr");
        add("umacr");
        add("Uogon");
        add("uogon");
        add("Uring");
        add("uring");
        add("Utilde");
        add("utilde");
        add("Wcirc");
        add("wcirc");
        add("Ycirc");
        add("ycirc");
        add("Yuml");
        add("Zacute");
        add("zacute");
        add("Zcaron");
        add("zcaron");
        add("Zdot");
        add("zdot");
        add("amp");
        add("apos");
        add("ast");
        add("brvbar");
        add("bsol");
        add("cent");
        add("colon");
        add("comma");
        add("commat");
        add("copy");
        add("curren");
        add("darr");
        add("deg");
        add("divide");
        add("dollar");
        add("equals");
        add("excl");
        add("frac12");
        add("frac14");
        add("frac18");
        add("frac34");
        add("frac38");
        add("frac58");
        add("frac78");
        add("gt");
        add("half");
        add("horbar");
        add("hyphen");
        add("iexcl");
        add("iquest");
        add("laquo");
        add("larr");
        add("lcub");
        add("ldquo");
        add("lowbar");
        add("lpar");
        add("lsqb");
        add("lsquo");
        add("lt");
        add("micro");
        add("middot");
        add("nbsp");
        add("not");
        add("num");
        add("ohm");
        add("ordf");
        add("ordm");
        add("para");
        add("percnt");
        add("period");
        add("plus");
        add("plusmn");
        add("pound");
        add("quest");
        add("quot");
        add("raquo");
        add("rarr");
        add("rcub");
        add("rdquo");
        add("reg");
        add("rpar");
        add("rsqb");
        add("rsquo");
        add("sect");
        add("semi");
        add("shy");
        add("sol");
        add("sung");
        add("sup1");
        add("sup2");
        add("sup3");
        add("times");
        add("trade");
        add("uarr");
        add("verbar");
        add("yen");
        add("blank");
        add("blk12");
        add("blk14");
        add("blk34");
        add("block");
        add("bull");
        add("caret");
        add("check");
        add("cir");
        add("clubs");
        add("copysr");
        add("cross");
        add("Dagger");
        add("dagger");
        add("dash");
        add("diams");
        add("dlcrop");
        add("drcrop");
        add("dtri");
        add("dtrif");
        add("emsp");
        add("emsp13");
        add("emsp14");
        add("ensp");
        add("female");
        add("ffilig");
        add("fflig");
        add("ffllig");
        add("filig");
        add("flat");
        add("fllig");
        add("frac13");
        add("frac15");
        add("frac16");
        add("frac23");
        add("frac25");
        add("frac35");
        add("frac45");
        add("frac56");
        add("hairsp");
        add("hearts");
        add("hellip");
        add("hybull");
        add("incare");
        add("ldquor");
        add("lhblk");
        add("loz");
        add("lozf");
        add("lsquor");
        add("ltri");
        add("ltrif");
        add("male");
        add("malt");
        add("marker");
        add("mdash");
        add("mldr");
        add("natur");
        add("ndash");
        add("nldr");
        add("numsp");
        add("phone");
        add("puncsp");
        add("rdquor");
        add("rect");
        add("rsquor");
        add("rtri");
        add("rtrif");
        add("rx");
        add("sext");
        add("sharp");
        add("spades");
        add("squ");
        add("squf");
        add("star");
        add("starf");
        add("target");
        add("telrec");
        add("thinsp");
        add("uhblk");
        add("ulcrop");
        add("urcrop");
        add("utri");
        add("utrif");
        add("vellip");
        add("aleph");
        add("and");
        add("ang90");
        add("angsph");
        add("angst");
        add("ap");
        add("becaus");
        add("bernou");
        add("bottom");
        add("cap");
        add("compfn");
        add("cong");
        add("conint");
        add("cup");
        add("Dot");
        add("DotDot");
        add("equiv");
        add("exist");
        add("fnof");
        add("forall");
        add("ge");
        add("hamilt");
        add("iff");
        add("infin");
        add("int");
        add("isin");
        add("lagran");
        add("lang");
        add("lArr");
        add("le");
        add("lowast");
        add("minus");
        add("mnplus");
        add("nabla");
        add("ne");
        add("ni");
        add("notin");
        add("or");
        add("order");
        add("par");
        add("part");
        add("permil");
        add("perp");
        add("phmmat");
        add("Prime");
        add("prime");
        add("prop");
        add("radic");
        add("rang");
        add("rArr");
        add("sim");
        add("sime");
        add("square");
        add("sub");
        add("sube");
        add("sup");
        add("supe");
        add("tdot");
        add("there4");
        add("tprime");
        add("Verbar");
        add("wedgeq");
    }};

    /**
     * The name of the DocBook title element
     */
    public static final String DOCBOOK_TITLE_ELEMENT = "title";
    /**
     * The name of the DocBook section element
     */
    public static final String DOCBOOK_SECTION_ELEMENT = "section";

    /**
     * Time, in milliseonds, between messages that say the client could not access the REST server
     */
    public static final int REST_SERVER_ERROR_MESSAGE_DELAY = 10000;

    /**
     * Monospaced web safe fonts
     */
    public static final String[] MONOSPACED_FONTS = new String[] {

        "Andale Mono WT",
        "Andale Mono",
        "Bitstream Vera Sans Mono",
        "Consolas",
        "Courier New",
        "Courier",
        "DejaVu Sans Mono",
        "Droid Sans Mono",
        "Everson Mono",
        "Fixed",
        "Fixedsys",
        "FreeMono",
        "HyperFont",
        "Inconsolata",
        "Letter Gothic",
        "Liberation Mono",
        "Lucida Console",
        "Lucida Sans Typewriter",
        "Lucida Typewriter",
        "Menlo",
        "Monaco",
        "monofur",
        "monospace",
        "Nimbus Mono L",
        "Prestige Elite",
        "ProFont",
        "PT Mono",
        "Ubuntu Mono"
    };

    /**
     * The default fallback for all font selections
     */
    public static final String DEFAULT_MONOSPACED_FONT = "monospace";

    /**
     * The available font sizes
     */
    public static final String[] FONT_SIZES = new String[] {
        "10px",
        "11px",
        "12px",
        "13px",
        "14px",
        "16px",
        "18px",
        "20px"
    };

    public static final List<RESTCSNodeTypeV1> TOPIC_CS_NODE_TYPES = Arrays.asList(RESTCSNodeTypeV1.INITIAL_CONTENT_TOPIC,
            RESTCSNodeTypeV1.META_DATA_TOPIC,
            RESTCSNodeTypeV1.TOPIC);

    /**
     * The attribute that holds a DocBook condition
     */
    public static final String CONDITION_ATTRIBUTE = "condition";

    /**
     * The attribute that is used by the help overlay system
     */
    public static final String PRESSGANG_WEBSITES_HELP_OVERLAY_DATA_ATTR = "data-pressgangtopic";

    /**
     * The default ace editor theme
     */
    public static final String DEFAULT_THEME = "eclipse";

    /**
     * The default ace editor theme
     */
    public static final String DEFAULT_FONT_SIZE = "12px";

    /**
     * The height of the tabs in a tab panel, in px
     */
    public static final float TAB_PANEL_HEIGHT = 35;
    /**
     * The units that the height of the table is measured in
     */
    public static final Style.Unit TAB_PANEL_HEIGHT_FORMAT = Style.Unit.PX;

    /**
     * The JSON Key for a recently failed failed server id
     */
    public static final String FAILED_SERVER_ID = "FailedServerId";

    /**
     * The JSON Key for a recently failed failed server time
     */
    public static final String FAILED_SERVER_TIME = "FailedServerTime";

    /**
     * How long to remember recently failed servers in milliseconds
     */
    public static final int REMEMBER_RECENTLY_FAILED_SERVERS = 30000;

    /**
     * The time in milliseconds to wait for a rest call to complete.
     */
    public static final int REST_CALL_TIMEOUT = 60000;

    /**
     * This is some common text prefixed to any error response generated by the server. This
     * allows clients to distinguish between a generic error and something generated by Pressgang.
     */
    public static final String ERROR_TEXT_PREFIX = "PressGang Exception.";

    /**
     * The minimum size of the split size panels, to prevent them from being
     * resized to 0.
     */
    public static final int MINIMUM_SPLIT_SIZE = 50;

    /**
     * The query parameter to append to the echo xml endpoint, which will allow the returned XML
     * to communicate with the parent server across domains once it is parsed to HTML.
     */
    public static final String ECHO_ENDPOINT_PARENT_DOMAIN_QUERY_PARAM = "parentDomain";

    /**
     * XML Mime type
     */
    public static final String XML_MIME_TYPE = "application/xml";

    /**
     * Plain Text Mime type
     */
    public static final String PLAIN_TEXT_MIME_TYPE = "text/plain";

    /**
     * The maximum length of the title before it is truncated
     */
    public static final int MAX_PAGE_TITLE_LENGTH = 20;

    /**
     * The prefix used to identify a topic's initial revision
     */
    public static final String REVISION_TOPIC_VIEW_DATA_PREFIX = "r:";

    /**
     * The sort order to apply to a newly added child.
     */
    public static final int NEW_CHILD_SORT_ORDER = 0;
    /**
     * The first sort order used when setting the sort order to an existing list.
     */
    public static final int CHILDREN_SORT_ORDER_START = 1;
    /**
     * The number of rows to "fast forward" by with the SimplePager.
     */
    public static final int FAST_FORWARD_ROWS = 100;
    /**
     * An ID that can not appear in the database.
     */
    public static final int NULL_ID = -1;
    /**
     * How long to wait before refreshing the rendered view (in milliseconds).
     */
    public static final int REFRESH_RATE = 5000;
    /**
     * How long to wait before refreshing the rendered view with images (in milliseconds).
     */
    public static final int REFRESH_RATE_WTH_IMAGES = 10000;
    /**
     * The name of a javascript click event.
     */
    public static final String JAVASCRIPT_CLICK_EVENT = "click";
    /**
     * All path segments that define a query must start with this string.
     */
    public static final String QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON = "query";
    /**
     * All path segments that define a query must start with this string.
     */
    public static final String QUERY_PATH_SEGMENT_PREFIX = QUERY_PATH_SEGMENT_PREFIX_WO_SEMICOLON + ";";
    /**
     * All path segments that define a query must start with this string.
     */
    public static final String CREATE_PATH_SEGMENT_PREFIX_WO_SEMICOLON = "create";
    /**
     * All path segments that define a query must start with this string.
     */
    public static final String CREATE_PATH_SEGMENT_PREFIX = CREATE_PATH_SEGMENT_PREFIX_WO_SEMICOLON + ";";
    /**
     * This path segment defines the initial topic view state.
     */
    public static final String TOPIC_VIEW_DATA_PREFIX_WO_SEMICOLON = "topicViewData";
    /**
     * This path segment defines the initial topic view state.
     */
    public static final String TOPIC_VIEW_DATA_PREFIX = TOPIC_VIEW_DATA_PREFIX_WO_SEMICOLON + ";";

    public static final String ENTITY_ID_PREFIX_WO_SEMICOLON = "id";
    public static final String ENTITY_ID_PREFIX = ENTITY_ID_PREFIX_WO_SEMICOLON + ";";
    public static final String ACTION_PREFIX_WO_SEMICOLON = "action";
    public static final String ACTION_PREFIX = ACTION_PREFIX_WO_SEMICOLON + ";";

    /**
     * The deafult size of the xml error split panels.
     */
    public static final int XML_ERRORS_SPLIT_PANEL_SIZE = 64;
    /**
     * The size of the split panels.
     */
    public static final double SPLIT_PANEL_SIZE = 300;
    /**
     * The height of the header banner in the template.
     */
    public static final int HEADING_BANNER_HEIGHT = 60;
    /**
     * The size of the split panel dividers.
     */
    public static final int SPLIT_PANEL_DIVIDER_SIZE = 5;

    /**
     * The height of the container holding the action bars. This is larger than height defined in the
     * .TopActionParentPanel class to account for the scrollbar
     */
    public static final int ACTION_BAR_PARENT_HEIGHT = 87;
    /**
     * The height of the footer.
     */
    public static final int FOOTER_HEIGHT = 40;
    /**
     * The maximum number of results to return in a search result.
     */
    public static final int MAX_SEARCH_RESULTS = 15;
    /**
     * The report that run a report with no other options.
     */
    public static final String BIRT_RUN_REPORT = "run?__report=";
    /**
     * The report that displays the topics bugzilla bugs.
     */
    public static final String BIRT_TOPIC_BUGZILLA_REPORT = "General/Bugs_Per_Topic.rptdesign&ShowOnlyData=True&AlternateTableHeaderBackgroundColour=True&TopicIDParameter=";
    /**
     * The DocBuilder server
     */
    public static final String DOCBUILDER_SERVER = "http://docbuilder.usersys.redhat.com";

    /**
     * Old versions of the rest interface did not accept encode text for queries. New versions do. Set to
     * true for the new REST API, and false for older versions.
     */
    public static final boolean ENCODE_QUERY_OPTIONS = true;

    public static final String TYPO_JS_DIR_LOCATION = GWT.getHostPageBaseURL() + "javascript/typojs/";

    /**
     * The reference to the XSL file, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single.xsl'?>";
    /**
     * The reference to the XSL file which rendered remarks, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_REMARKS_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single-remarks.xsl'?>";

    /**
     * The reference to the XSL file that will post back the rendered HTML, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_DIFF_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single-diff.xsl'?>";

    /**
     * The reference to the XSL file that will post back the rendered HTML, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_RENDER_ONLY_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single-renderonly.xsl'?>";

    /**
     * The reference to the XSL file with placeholder images, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_PLACEHOLDER_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single-placeholder.xsl'?>";

    /**
     * The reference to the XSL file with placeholder images and remarks, to be added to any XML being rendered by the browser
     */
    public static final String DOCBOOK_REMARKS_PLACEHOLDER_XSL_REFERENCE = "<?xml-stylesheet type='text/xsl' href='/pressgang-ccms-static/publican-docbook/html-single-remarks-placeholder.xsl'?>";
    /**
     * The REST endpoint that echos previously submitted xml
     */
    public static final String ECHO_ENDPOINT = "/1/echoxml";

    /**
     * The base URL to Bugzilla.
     */
    public static final String BUGZILLA_BASE_URL = "https://bugzilla.redhat.com";
    /**
     * The Bugzilla link for this product.
     */
    public static final String BUGZILLA_URL = BUGZILLA_BASE_URL + "/enter_bug.cgi?product=PressGang CCMS&component=Web-UI&version=1" +
            ".8&cf_build_id=UI%20Build%20" + BUILD;
    /**
     * A link to the survey.
     */
    public static final String KEY_SURVEY_LINK = "http://www.keysurvey.com/f/457744/149e/?LQID=1&source=";
    /**
     * The start of the url for the documentation of Docbook XML elements.
     */
    public static final String DOCBOOK_ELEMENT_URL_PREFIX = "http://www.docbook.org/tdg5/en/html/";
    /**
     * The end of the url for the documentation of Docbook XML elements.
     */
    public static final String DOCBOOK_ELEMENT_URL_POSTFIX = ".html";
    /**
     * The width of the help dialog box.
     */
    public static final String HELP_DIALOG_WIDTH = "640px";
    /**
     * The height of the help dialog box.
     */
    public static final String HELP_DIALOG_HEIGHT = "480px";
    /**
     * The width of the bulk topic dialog box.
     */
    public static final String BULK_IMPORT_DIALOG_WIDTH = "800px";
    /**
     * The height of the bulk topic dialog box.
     */
    public static final String BULK_IMPORT_DIALOG_HEIGHT = "480px";
    /**
     * The width of the bulk topic dialog box.
     */
    public static final String BULK_OVERWRITE_DIALOG_WIDTH = "400px";
    /**
     * The height of the bulk topic dialog box.
     */
    public static final String BULK_OVERWRITE_DIALOG_HEIGHT = "120px";
    /**
     * A line break escaped.
     */
    public static final String LINE_BREAK_ESCAPED = "\\n";
    /**
     * A carriage return and line break escaped.
     */
    public static final String CARRIAGE_RETURN_AND_LINE_BREAK_ESCAPED = "\\r\\n";
    /**
     * A line break.
     */
    public static final String LINE_BREAK = "\n";
    /**
     * A carriage return and line break.
     */
    public static final String CARRIAGE_RETURN_AND_LINE_BREAK = "\r\n";
    /**
     * A comma.
     */
    public static final String COMMA = ",";
    /**
     * The category internal "and" logic to apply when searching. The "or" logic is !DEFAULT_INTERNAL_AND_LOGIC
     */
    public static final boolean DEFAULT_INTERNAL_AND_LOGIC = false;
    /**
     * The category external "and" category logic to apply when searching. The "or" logic is !DEFAULT_EXTERNAL_AND_LOGIC
     */
    public static final boolean DEFAULT_EXTERNAL_AND_LOGIC = true;
    /**
     * This is the value for the category logic option "and", as passed by the query strings.
     */
    public static final String AND_LOGIC_QUERY_STRING_VALUE = "And";
    /**
     * This is the value for the category logic option "or", as passed by the query strings.
     */
    public static final String OR_LOGIC_QUERY_STRING_VALUE = "Or";

    /*
        Navigation Button ID's
     */
    public enum ElementIDs {
        HOME_NAVIGATION_BUTTON_ID("HomeNavigationButton"),
        DOCBUILDER_NAVIGATION_BUTTON_ID("DocBuilderNavigationButton"),
        CREATE_TOPIC_NAVIGATION_BUTTON_ID("CreateTopicBuilderNavigationButton"),
        CREATE_SPEC_NAVIGATION_BUTTON_ID("CreateSpecNavigationButton"),
        REPORTS_NAVIGATION_BUTTON_ID("ReportsNavigationButton"),
        CREATE_BUG_NAVIGATION_BUTTON_ID("CreateBugNavigationButton"),
        SEARCH_NAVIGATION_BUTTON_ID("SearchNavigationButton"),
        SEARCH_TOPICS_NAVIGATION_BUTTON_ID("SearchTopicsNavigationButton"),
        SEARCH_CONTENT_SPECS_NAVIGATION_BUTTON_ID("SearchContentSpecsNavigationButton"),
        SEARCH_TRANSLATIONS_NAVIGATION_BUTTON_ID("SearchTranslationsNavigationButton"),
        ENTITIES_NAVIGATION_BUTTON_ID("EntitiesNavigationButton"),
        IMAGES_NAVIGATION_BUTTON_ID("ImagesNavigationButton"),
        FILES_NAVIGATION_BUTTON_ID("FilesNavigationButton"),
        TAGS_NAVIGATION_BUTTON_ID("TagsNavigationButton"),
        CATEGORIES_NAVIGATION_BUTTON_ID("CategoriesNavigationButton") ,
        PROJECTS_NAVIGATION_BUTTON_ID("ProjectsNavigationButton"),
        ADVANCED_NAVIGATION_BUTTON_ID("AdvancedNavigationButton"),
        BULK_TAGGING_NAVIGATION_BUTTON_ID("BulkTaggingNavigationButton"),
        BLOB_CONSTANTS_NAVIGATION_BUTTON_ID("BlobConstantsNavigationButton"),
        INTEGER_CONSTANTS_NAVIGATION_BUTTON_ID("IntegerConstantsNavigationButton"),
        STRING_CONSTANTS_NAVIGATION_BUTTON_ID("StringConstantsNavigationButton"),
        EXTENDED_PROPERTIES_NAVIGATION_BUTTON_ID("ExtendedPropertiesNavigationButton"),
        EXTENDED_PROPERTY_CATEGORIES_NAVIGATION_BUTTON_ID("ExtendedPropertyCategoriesNavigationButton"),
        MONITORING_NAVIGATION_BUTTON_ID("MonitoringNavigationButton"),
        USERNAME_SAVE_DIALOG("UsernameSaveDialog"),
        MAX_TOPIC_REV_DIALOG("MaxTopicRevisionDialog"),
        MESSAGE_SAVE_DIALOG("MessageSaveDialog"),
        MAJOR_CHANGE_SAVE_DIALOG("MajorChangeSaveDialog"),
        MINOR_CHANGE_SAVE_DIALOG("MinorChangeSaveDialog"),
        CONTENT_SPEC_LIST_SAVE_DIALOG("ContentSpecListSaveDialog"),
        OK_SAVE_DIALOG("OKSaveDialog"),
        CANCEL_SAVE_DIALOG("CancelSaveDialog"),
        RENDERED_PANE_TOPIC_EDIT_BUTTON_ID("RenderedPaneTopicEditButton"),
        RENDERED_VIEW_TOPIC_EDIT_BUTTON_ID("RenderedViewTopicEditButton"),
        XML_EDITING_TOPIC_EDIT_BUTTON_ID("XMLEditingTopicEditButton"),
        PROPERTIES_TOPIC_EDIT_BUTTON_ID("PropertiesTopicEditButton"),
        EXTENDED_PROPERTIES_TOPIC_EDIT_BUTTON_ID("ExtendedPropertiesTopicEditButton"),
        SOURCE_URLS_TOPIC_EDIT_BUTTON_ID("SourceURLsTopicEditButton"),
        TAGS_TOPIC_EDIT_BUTTON_ID("TagsTopicEditButton"),
        BUGS_TOPIC_EDIT_BUTTON_ID("BugsTopicEditButton"),
        CONTENT_SPECS_TOPIC_EDIT_BUTTON_ID("ContentSpecTopicEditButton"),
        SAVE_TOPIC_EDIT_BUTTON_ID("SaveTopicEditButton"),
        REVISIONS_TOPIC_EDIT_BUTTON_ID("SaveTopicEditButton"),
        TOPIC_TITLE_PROPERTIES_VIEW_FIELD_ID("TopicTitlePropertiesViewField"),
        TOPIC_DESCRIPTION_PROPERTIES_VIEW_FIELD_ID("TopicDescriptionPropertiesViewField"),
        TOPIC_LOCALE_PROPERTIES_VIEW_FIELD_ID("TopicLocalePropertiesViewField");

                
        private final String id;
        private ElementIDs(@NotNull final String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * A private constructor to prevent instantiation.
     */
    private Constants() {

    }
}

