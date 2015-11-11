package tools.com.ibm.midi;

/***************************************************************************/
/*                                                                         */
/* (c) Copyright IBM Corp. 2001  All rights reserved.                      */
/*                                                                         */
/* This sample program is owned by International Business Machines         */
/* Corporation or one of its subsidiaries ("IBM") and is copyrighted       */
/* and licensed, not sold.                                                 */
/*                                                                         */
/* You may copy, modify, and distribute this sample program in any         */
/* form without payment to IBM, for any purpose including developing,      */
/* using, marketing or distributing programs that include or are           */
/* derivative works of the sample program.                                 */
/*                                                                         */
/* The sample program is provided to you on an "AS IS" basis, without      */
/* warranty of any kind.  IBM HEREBY  EXPRESSLY DISCLAIMS ALL WARRANTIES,  */
/* EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED   */
/* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.     */
/* Some jurisdictions do not allow for the exclusion or limitation of      */
/* implied warranties, so the above limitations or exclusions may not      */
/* apply to you.  IBM shall not be liable for any damages you suffer as    */
/* a result of using, modifying or distributing the sample program or      */
/* its derivatives.                                                        */
/*                                                                         */
/* Each copy of any portion of this sample program or any derivative       */
/* work,  must include the above copyright notice and disclaimer of        */
/* warranty.                                                               */
/*                                                                         */
/***************************************************************************/

/***************************************************************************/
/*                                                                         */
/* This file accompanies the article "Understanding and using Java MIDI    */
/* audio." This article was published in the Special Edition 2001 issue    */
/* of the IBM DeveloperToolbox Technical Magazine at                       */
/* http://www.developer.ibm.com/devcon/mag.htm.                            */
/*                                                                         */
/***************************************************************************/

//------------------------------------------------------------------------
// File Name:     BankInfo
// Description:   Inspect the instruments in a sound bank.
//------------------------------------------------------------------------
import java.io.File;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.SoundbankResource;

/**
 * BankInfo Lists the instruments in a sound bank.
 * 
 * @author Dan Becker
 */
public class BankInfo {

	/** Play given arguments as file names. */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage: java BankInfo [soundbank name]*");
			System.exit(1);
		}

		for (String soundBankName : args) {
			System.out.println("Opening sound bank " + soundBankName);

			File soundBankFile = new File(soundBankName);
			Soundbank soundBank = MidiSystem.getSoundbank(soundBankFile);

			System.out.println("Soundbank name: " + soundBank.getName());
			System.out.println("Soundbank vendor: " + soundBank.getVendor());
			System.out.println("Soundbank version: " + soundBank.getVersion());
			System.out.println("Soundbank description: " + soundBank.getDescription());

			SoundbankResource[] sbrs = soundBank.getResources();
			if ((sbrs == null) || (sbrs.length == 0)) {
				System.out.println("Soundbank resources: none");
			} else {
				for (int j = 0; j < sbrs.length; j++) {
					String name = sbrs[j].getName();
					if (name.endsWith("\n")) {
						name = name.trim();
					}
					System.out.println("Soundbank resource " + j + ": " + name);
				}
			}

			Instrument[] instruments = soundBank.getInstruments();
			if ((instruments == null) || (instruments.length == 0)) {
				System.out.println("Soundbank instruments: none");
			} else {
				for (int j = 0; j < instruments.length; j++) {
					String name = instruments[j].getName();
					if (name.endsWith("\n")) {
						name = name.trim();
					}
					System.out.println("Soundbank instrument " + j + ": " + name);
				}
			}
		} // for
		System.exit(0);
	} // main
} // class BankInfo

