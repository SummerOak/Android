/**
 * ****************************************************************************
 * Description : an interface to provide details of virtual view, something like visibility,position in parent,
 * position on screen...
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.graphics.Rect;
import android.view.View;

import java.util.List;

public interface IAccessable {

    AccessibilityDelegate getAccessibilityDelegator();

    /**
     * get the root View/ViewGroup this virtual view(usually a BaseView or BaseViewGroup) attached on.
     * @return
     */
    View getAdapterViewParent();

    boolean isVisible();

    boolean isCheckable();
    boolean isChecked();

    CharSequence getContentDescription();

    int getChildOffsetInParentX();
    int getChildOffsetInParentY();

    boolean pointInView(float x, float y);
    boolean getBoundsInParent(Rect out);
    boolean getBoundsOnScreenForAccessbility(Rect out);
    boolean getGlobalVisibleRectForAccessibility(Rect out);

    /**
     * the value returned is equal to {@link View#getImportantForAccessibility()}
     * @return
     */
    int getAccessibilityImportance();

    /**
     * @return in order to avoid too much null checking, ensure this return value is not null
     * even do not have any child, return an empty list rather than null.
     */
    List<IAccessable> getChildrenForAccessibility();
}
