package com.cs440.capstone;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class CursorJoiner {
	
	Cursor cursorA;
	Cursor cursorB;
	Context ourContext;
	
	public CursorJoiner(Cursor cursorA, Cursor cursorB, Context ourContext){
		this.cursorA = cursorA;
		this.cursorB = cursorB;
		this.ourContext = ourContext;
	}
	
	//Add everything in cursorB to cursorA
	public Cursor joinCursor(){	
		CampusDatabase db = new CampusDatabase(ourContext);
		db.open();
		Cursor c = db.getAllRows();
		return c;
	}

}
