package com.metamage.noisegate;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.metamage.noisegate.F;
import com.noysbrij.noisebridgeGeneral.*;

public class Key extends Button
{
	
	private boolean dragging    = false;
	private boolean outOfBounds = false;
	
	private View counterpart;
	
	public Key( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );
		
		init();
	}
	
	public Key( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		
		init();
	}
	
	public void init()
	{
		F.setKeyColor(this, Data.normalColor );
	}
	
	public void setCounterpart( int id )
	{
		Activity context = (Activity) getContext();
		
		counterpart = context.findViewById( id );
	}
	
	public Button getCounterpart()
	{
		if ( counterpart == null )
		{
			final int i = Character.digit( getText().charAt( 0 ), 10 );
			
			if ( i >= 0 )
			{
				final int id = Data.fakeKeyIds[ i ];
				
				setCounterpart( id );
			}
		}
		
		return (Button) counterpart;
	}
	
	public boolean isDragging()
	{
		return dragging;
	}
	
	private boolean hitFeedback()
	{
		return true;
	}
	
	public void beginDrag()
	{
		 F.setKeyColor( this, Data.pressedColor );
	}
	
	public void updateDrag( float x, float y )
	{
		final boolean inside = getBackground().getBounds().contains( (int) x, (int) y );
		
		if ( inside == outOfBounds )
		{
			int color;
			
			if ( inside )
			{
				color = Data.pressedColor;
			}
			else
			{
				color = Data.normalColor;
			}
			
			F.setKeyColor( this, color );
			
			outOfBounds = !inside;
		}
	}
	
	public void endDrag()
	{
		if ( !outOfBounds )
		{
			F.setKeyColor( this, Data.normalColor );
			 
		 	performClick();
		 	
		 	Button fakeKey = getCounterpart();
		 	
		 	if ( fakeKey != null )
		 	{
				F.setKeyColor( fakeKey, Data.pressedColor );
				
				F.fadeViewToAlpha( fakeKey, 0 );
		 	}
		}
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		final int action = event.getActionMasked();
		
		switch ( action )
		{
			case MotionEvent.ACTION_DOWN:
				break;
			
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				if ( dragging )
				{
					break;
				}
				
				// fall through
			
			default:
				return super.onTouchEvent( event );
		}
		
		final float x = event.getX();
		final float y = event.getY();
		
		if ( action == MotionEvent.ACTION_DOWN )
		{
			dragging    = true;
			outOfBounds = false;
			
			beginDrag();
			
			return hitFeedback();
		}
		else  // MOVE or UP
		{
			updateDrag( x, y );
			
			if ( action == MotionEvent.ACTION_UP )
			{
				endDrag();
				
				dragging = false;
			}
		}
		
		return true;
	}
}

