/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.anzsoft.client.utils;

import com.anzsoft.client.utils.TextUtils;
import com.anzsoft.client.utils.emotions.Emoticons;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

public class ChatTextFormatter {

    private static final String JOYFUL = "KuneProtIniJOYFULKuneProtEnd";
    private static final String ANGRY = "KuneProtIniANGRYKuneProtEnd";
    private static final String BLUSHING = "KuneProtIniBLUSHINGKuneProtEnd";
    private static final String CRYING = "KuneProtIniCRYINGKuneProtEnd";
    private static final String POUTY = "KuneProtIniPOUTYKuneProtEnd";
    private static final String SURPRISED = "KuneProtIniSURPRISEDKuneProtEnd";
    private static final String GRIN = "KuneProtIniGRINKuneProtEnd";
    private static final String ANGEL = "KuneProtIniANGELKuneProtEnd";
    private static final String KISSING = "KuneProtIniKISSINGKuneProtEnd";
    private static final String SMILE = "KuneProtIniSMILEKuneProtEnd";
    private static final String TONGUE = "KuneProtIniTONGUEKuneProtEnd";
    private static final String UNCERTAIN = "KuneProtIniUNCERTAINKuneProtEnd";
    private static final String COOL = "KuneProtIniCOOLKuneProtEnd";
    private static final String WINK = "KuneProtIniWINKKuneProtEnd";
    private static final String HAPPY = "KuneProtIniHAPPYKuneProtEnd";
    private static final String ALIEN = "KuneProtIniALIENKuneProtEnd";
    private static final String ANDY = "KuneProtIniANDYKuneProtEnd";
    private static final String DEVIL = "KuneProtIniDEVILKuneProtEnd";
    private static final String LOL = "KuneProtIniLOLKuneProtEnd";
    private static final String NINJA = "KuneProtIniNINJAKuneProtEnd";
    private static final String SAD = "KuneProtIniSADKuneProtEnd";
    private static final String SICK = "KuneProtIniSICKKuneProtEnd";
    private static final String SIDEWAYS = "KuneProtIniSIDEWAYSKuneProtEnd";
    private static final String SLEEPING = "KuneProtIniSLEEPINGKuneProtEnd";
    private static final String UNSURE = "KuneProtIniUNSUREKuneProtEnd";
    private static final String WONDERING = "KuneProtIniWONDERINGKuneProtEnd";
    private static final String LOVE = "KuneProtIniLOVEKuneProtEnd";
    private static final String PINCHED = "KuneProtIniPINCHEDKuneProtEnd";
    private static final String POLICEMAN = "KuneProtIniPOLICEMANKuneProtEnd";
    private static final String W00T = "KuneProtIniWOOTKuneProtEnd";
    private static final String WHISTLING = "KuneProtIniWHISLINGKuneProtEnd";
    private static final String WIZARD = "KuneProtIniWIZARDKuneProtEnd";
    private static final String BANDIT = "KuneProtIniBANDITKuneProtEnd";
    private static final String HEART = "KuneProtIniHEARTKuneProtectRepEnd";

    public static HTML format(final String messageOrig) {
        String message = messageOrig;
        message = escapeHtmlLight(message);
        message = message.replaceAll("\n", "<br>\n");
        message = formatUrls(message);
        message = formatEmoticons(message);

        return new HTML(message);
    }

    static String formatUrls(String message) {
        return message = message.replaceAll(TextUtils.URL_REGEXP, "<a href=\"$1\" target=\"_blank\">$1</a>");
    }
    
    static String escapeHtmlLight(String textOrig)
    {
    	String text = textOrig;
    	text = text.replaceAll("&", "&amp;");
    	text = text.replaceAll("\"", "&quot;");
    	text = text.replaceAll("<", "&lt;");
    	text = text.replaceAll(">", "&gt;");
    	return text;
    }

    static String preFormatEmoticons(String message) {
        message = replace(message, new String[] { "&gt;:\\)" }, DEVIL);
        message = replace(message, new String[] { "O:\\)", "o:\\)", "o:-\\)", "O:-\\)", "0:\\)", "0:-\\)" }, ANGEL);
        message = replace(message, new String[] { "\\^_\\^", "\\^-\\^", "\\^\\^", ":\\)\\)", ":-\\)\\)" }, JOYFUL);
        message = replace(message, new String[] { "\\(police\\)", "\\(cop\\)", "\\{\\):\\)" }, POLICEMAN);
        message = replace(message, new String[] { "=:\\)", "\\(alien\\)" }, ALIEN);
        message = replace(message, new String[] { "o_O", "o_0", "O_O", "o_o", "O_o", "0_o", "o0", "0o", "oO", "Oo",
                "0_0" }, ANDY);
        message = replace(message, new String[] { "&gt;:o", "&gt;:-o", "&gt;:O", "&gt;:-O", "X\\(", "X-\\(", "x\\(",
                "x-\\(", ":@", "&lt;_&lt;" }, ANGRY);
        message = replace(message, new String[] { "\\(bandit\\)", ":\\(&gt;" }, BANDIT);
        message = replace(message, new String[] { ":&quot;&gt;", ":\\*&gt;", ":-\\$", ":\\$" }, BLUSHING);
        message = replace(message, new String[] { "B\\)", "B-\\)", "8\\)" }, COOL);
        message = replace(message, new String[] { ":\'\\(", "=\'\\(" }, CRYING);
        message = replace(message, new String[] { ":-d", ":d", ":-D", ":D", ":d", "=D", "=-D" }, GRIN);
        message = replace(message, new String[] { "=\\)", "=-\\)" }, HAPPY);
        message = replace(message, new String[] { "\\(L\\)", "\\(h\\)", "\\(H\\)" }, HEART);
        message = replace(message, new String[] { ":-\\*", ":\\*" }, KISSING);
        message = replace(message, new String[] { "\\(LOL\\)", "lol" }, LOL);
        message = replace(message, new String[] { ":-X", ":-xX", ":x", "\\(wubya\\)", "\\(wubyou\\)", "\\(wub\\)" },
                LOVE);
        message = replace(message, new String[] { "\\(:\\)", "\\(ph33r\\)", "\\(ph34r\\)" }, NINJA);
        message = replace(message, new String[] { "&gt;_&lt;" }, PINCHED);
        message = replace(message, new String[] { ":\\|", "=\\|", ":-\\|" }, POUTY);
        message = replace(message, new String[] { ":\\(", "=\\(", "=-\\(", ":-\\(" }, SAD);
        message = replace(message, new String[] { ":&amp;", ":-&amp;" }, SICK);
        message = replace(message, new String[] { "=]" }, SIDEWAYS);
        message = replace(message, new String[] { "\\(-.-\\)", "\\|\\)", "\\|-\\)", "I-\\)", "I-\\|" }, SLEEPING);
        message = replace(message, new String[] { ":-O", ":O", ":-o", ":o", ":-0", "=-O", "=-o", "=o", "=O" },
                SURPRISED);
        message = replace(message, new String[] { ":P", "=P", "=p", ":-P", ":p", ":-p", ":b" }, TONGUE);
        message = replace(message, new String[] { ":-\\\\", ":-/", ":/", ":\\\\" }, UNCERTAIN);
        message = replace(message, new String[] { ":s", ":-S", ":-s", ":S" }, UNSURE);
        message = replace(message, new String[] { "\\(woot\\)", "\\(w00t\\)", "\\(wOOt\\)" }, W00T);
        message = replace(message, new String[] { ":-&quot;" }, WHISTLING);
        message = replace(message, new String[] { ";\\)", ";-\\)", ";&gt;" }, WINK);
        message = replace(message, new String[] { "\\(wizard\\)" }, WIZARD);
        message = replace(message, new String[] { ":\\?" }, WONDERING);
        message = replace(message, new String[] { ":-\\)", ":\\)" }, SMILE);
        return message;
    }

    private static String formatEmoticons(String message) {
        final Emoticons img = Emoticons.App.getInstance();

        message = preFormatEmoticons(message);
        
        message = message.replaceAll(SMILE, getImgHtml(img.smile()));
        message = message.replaceAll(CRYING, getImgHtml(img.crying()));
        message = message.replaceAll(SURPRISED, getImgHtml(img.surprised()));
        message = message.replaceAll(ANGEL, getImgHtml(img.angel()));
        message = message.replaceAll(HAPPY, getImgHtml(img.happy()));
        message = message.replaceAll(GRIN, getImgHtml(img.grin()));
        message = message.replaceAll(JOYFUL, getImgHtml(img.joyful()));
        message = message.replaceAll(UNCERTAIN, getImgHtml(img.uncertain()));
        message = message.replaceAll(ANGRY, getImgHtml(img.angry()));
        message = message.replaceAll(TONGUE, getImgHtml(img.tongue()));
        message = message.replaceAll(LOVE, getImgHtml(img.love()));
        message = message.replaceAll(SLEEPING, getImgHtml(img.sleeping()));
        message = message.replaceAll(COOL, getImgHtml(img.cool()));
        message = message.replaceAll(KISSING, getImgHtml(img.kissing()));
        message = message.replaceAll(SAD, getImgHtml(img.sad()));
        message = message.replaceAll(ALIEN, getImgHtml(img.alien()));
        message = message.replaceAll(ANDY, getImgHtml(img.andy()));
        message = message.replaceAll(BANDIT, getImgHtml(img.bandit()));
        message = message.replaceAll(BLUSHING, getImgHtml(img.blushing()));
        message = message.replaceAll(DEVIL, getImgHtml(img.devil()));
        message = message.replaceAll(HEART, getImgHtml(img.heart()));
        message = message.replaceAll(LOL, getImgHtml(img.lol()));
        message = message.replaceAll(NINJA, getImgHtml(img.ninja()));
        message = message.replaceAll(PINCHED, getImgHtml(img.pinched()));
        message = message.replaceAll(POLICEMAN, getImgHtml(img.policeman()));
        message = message.replaceAll(POUTY, getImgHtml(img.pouty()));
        message = message.replaceAll(SICK, getImgHtml(img.sick()));
        message = message.replaceAll(SIDEWAYS, getImgHtml(img.sideways()));
        message = message.replaceAll(UNSURE, getImgHtml(img.unsure()));
        message = message.replaceAll(W00T, getImgHtml(img.w00t()));
        message = message.replaceAll(WINK, getImgHtml(img.wink()));
        message = message.replaceAll(WONDERING, getImgHtml(img.wondering()));
        message = message.replaceAll(WHISTLING, getImgHtml(img.whistling()));
        message = message.replaceAll(WIZARD, getImgHtml(img.wizard()));

        return message;
    }

    private static String getImgHtml(final AbstractImagePrototype img) {
        final Image image = new Image();
        DOM.setStyleAttribute(image.getElement(), "vertical-align", "bottom");
        img.applyTo(image);
        image.setStyleName("vamiddle");
        return img.getHTML();
    }

    private static String replace(String message, final String[] from, final String to) {
        for (int j = 0; j < from.length; j++) {
            message = message.replaceAll("(^|[\\s])" + from[j] + "([\\s]|$)", "$1" + to + "$2");
            // two times for: :) :) :) :)
            message = message.replaceAll("(^|[\\s])" + from[j] + "([\\s]|$)", "$1" + to + "$2");
        }
        return message;
    }

    public ChatTextFormatter() {
    }

}
