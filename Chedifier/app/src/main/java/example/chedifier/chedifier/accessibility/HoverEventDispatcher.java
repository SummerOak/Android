/**
 * ****************************************************************************
 * Description : dispatch hover event
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.util.Log;
import android.view.MotionEvent;

public class HoverEventDispatcher {

    private static final String TAG = "HoverEventStrategy";

    private static final boolean DEBUG = false;

    private IHoverLogicProvider mProvider;

    private HoverTarget mLastHoveredChilds;
    private boolean mHoveredSelf;

    public HoverEventDispatcher(IHoverLogicProvider hoverable){
        mProvider = hoverable;

        if(mProvider == null){
            throw new NullPointerException("mProvider can not be null.");
        }
    }

    public boolean hasHoveredChild(){
        return mLastHoveredChilds != null;
    }

    public boolean dispatchHoverEvent(MotionEvent event){
        if(event == null){
            return false;
        }

        final int action = event.getAction();
        boolean handled = false;
        MotionEvent eventNoHistory;

        HoverTarget firstOldHoverTarget = mLastHoveredChilds;
        mLastHoveredChilds = null;
        if(action != MotionEvent.ACTION_HOVER_EXIT
                && mProvider.getChildCount() >0){

            HoverTarget lastHoverTarget = null;
            for(int i = 0; i< mProvider.getChildCount(); i++){
                Object child = mProvider.getChildAt(i);
                if(!mProvider.isChildHoverableCurrent(child) ||
                        !mProvider.isChildUnderPoint(child,event.getX(),event.getY())){
                    continue;
                }

                HoverTarget hoverTarget = firstOldHoverTarget;
                final boolean wasHovered;
                for(HoverTarget predecessor=null;;){
                    if (hoverTarget == null) {
                        hoverTarget = HoverTarget.obtain(child);
                        wasHovered = false;
                        break;
                    }

                    if (hoverTarget.target == child) {
                        if (predecessor != null) {
                            predecessor.next = hoverTarget.next;
                        } else {
                            firstOldHoverTarget = hoverTarget.next;
                        }
                        hoverTarget.next = null;
                        wasHovered = true;
                        break;
                    }

                    predecessor = hoverTarget;
                    hoverTarget = hoverTarget.next;
                }

                // Enqueue the hover target onto the new hover target list.
                if (lastHoverTarget != null) {
                    lastHoverTarget.next = hoverTarget;
                } else {
                    mLastHoveredChilds = hoverTarget;
                }
                lastHoverTarget = hoverTarget;


                event.offsetLocation(-mProvider.getChildOffsetInParentX(child),
                        -mProvider.getChildOffsetInParentY(child));

                if (action == MotionEvent.ACTION_HOVER_ENTER) {
                    if (!wasHovered) {
                        handled |= dispatchTransformedGenericPointerEvent(event, child);
                    }
                } else if (action == MotionEvent.ACTION_HOVER_MOVE) {
                    if (!wasHovered) {
                        eventNoHistory = MotionEvent.obtainNoHistory(event);
                        eventNoHistory.setAction(MotionEvent.ACTION_HOVER_ENTER);
                        handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child); //enter
                        eventNoHistory.setAction(action);
                        handled |= dispatchTransformedGenericPointerEvent(eventNoHistory, child); // move
                    } else {
                        // Send the move as is.
                        handled |= dispatchTransformedGenericPointerEvent(event, child);
                    }
                }

                event.offsetLocation(mProvider.getChildOffsetInParentX(child),
                        mProvider.getChildOffsetInParentY(child));

                if (handled) {
                    break;
                }
            }
        }

        // Send exit events to all previously hovered children that are no longer hovered.
        if(DEBUG) Log.d(TAG,"firstOldHoverTarget " + firstOldHoverTarget);
        while (firstOldHoverTarget != null) {
            final Object child = firstOldHoverTarget.target;
            event.offsetLocation(-mProvider.getChildOffsetInParentX(child),
                    -mProvider.getChildOffsetInParentY(child));
            // Exit the old hovered child.
            if (action == MotionEvent.ACTION_HOVER_EXIT) {
                // Send the exit as is.
                handled |= dispatchTransformedGenericPointerEvent(event, child); // exit
            } else {
                // Synthesize an exit from a move or enter.
                // Ignore the result because hover focus has moved to a different view.
                if (action == MotionEvent.ACTION_HOVER_MOVE) {
                    dispatchTransformedGenericPointerEvent(event, child); // move
                }
                eventNoHistory = MotionEvent.obtainNoHistory(event);
                eventNoHistory.setAction(MotionEvent.ACTION_HOVER_EXIT);
                dispatchTransformedGenericPointerEvent(eventNoHistory, child); // exit
            }

            event.offsetLocation(mProvider.getChildOffsetInParentX(child),
                    mProvider.getChildOffsetInParentY(child));

            final HoverTarget nextOldHoverTarget = firstOldHoverTarget.next;
            firstOldHoverTarget = nextOldHoverTarget;
        }

        // Send events to the view group itself if no children have handled it.
        boolean newHoveredSelf = !handled;
        if (newHoveredSelf == mHoveredSelf) {
            if (newHoveredSelf) {
                // Send event to the view group as before.
                handled |= mProvider.dispatchEventToSelf(event);
            }
        } else {
            if (mHoveredSelf) {
                // Exit the view group.
                if (action == MotionEvent.ACTION_HOVER_EXIT) {
                    // Send the exit as is.
                    handled |= mProvider.dispatchEventToSelf(event); // exit
                } else {
                    // Synthesize an exit from a move or enter.
                    // Ignore the result because hover focus is moving to a different view.
                    if (action == MotionEvent.ACTION_HOVER_MOVE) {
                        mProvider.dispatchEventToSelf(event); // move
                    }
                    eventNoHistory = MotionEvent.obtainNoHistory(event);
                    eventNoHistory.setAction(MotionEvent.ACTION_HOVER_EXIT);
                    mProvider.dispatchEventToSelf(eventNoHistory); // exit
                }
                mHoveredSelf = false;
            }

            if (newHoveredSelf) {
                // Enter the view group.
                if (action == MotionEvent.ACTION_HOVER_ENTER) {
                    // Send the enter as is.
                    handled |= mProvider.dispatchEventToSelf(event); // enter
                    mHoveredSelf = true;
                } else if (action == MotionEvent.ACTION_HOVER_MOVE) {
                    // Synthesize an enter from a move.
                    eventNoHistory = MotionEvent.obtainNoHistory(event);
                    eventNoHistory.setAction(MotionEvent.ACTION_HOVER_ENTER);
                    handled |= mProvider.dispatchEventToSelf(eventNoHistory); // enter
                    eventNoHistory.setAction(action);

                    handled |= mProvider.dispatchEventToSelf(eventNoHistory); // move
                    mHoveredSelf = true;
                }
            }
        }

        return handled;
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent event, Object child){
        return mProvider.dispatchEventToChild(child,event);
    }

    private static class HoverTarget{

        private HoverTarget(Object t){
            target = t;
        }

        public static HoverTarget obtain(Object t){
            return new HoverTarget(t);
        }

        Object target;
        HoverTarget next;
    }


    public interface IHoverLogicProvider<T>{
        int getChildCount();
        T getChildAt(int index);

        boolean isChildHoverableCurrent(T child);
        boolean isChildUnderPoint(T child, float x, float y);

        int getChildOffsetInParentX(T child);
        int getChildOffsetInParentY(T child);

        boolean dispatchEventToChild(T child, MotionEvent event); // onHoverEvent
        boolean dispatchEventToSelf(MotionEvent event);
    }

}
