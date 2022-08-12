/*
 CDDL HEADER START

 The contents of this file are subject to the terms of the
 Common Development and Distribution License (the "License").
 You may not use this file except in compliance with the License.

 You can obtain a copy of the license at
   https://opensource.org/licenses/CDDL-1.0.
 See the License for the specific language governing permissions
 and limitations under the License.

 When distributing Covered Code, include this CDDL HEADER in each
 file and include the License file at
    https://opensource.org/licenses/CDDL-1.0.
 If applicable, add the following below this CDDL HEADER, with the
 fields enclosed by brackets "[]" replaced with your own identifying
 information: Portions Copyright [yyyy] [name of copyright owner]

 CDDL HEADER END
 */
package self.lesfoster.framework.integration;

/**
 * Holds the selected object.
 *
 * @author Leslie L Foster
 */
public class SelectedObjectWrapper {

    private Object selectedObject;

    /**
     * @return the selectedObject
     */
    public Object getSelectedObject() {
        return selectedObject;
    }

    /**
     * @param selectedObject the selectedObject to set
     */
    public SelectedObjectWrapper setSelectedObject(Object selectedObject) {
        this.selectedObject = selectedObject;
        return this;
    }
}
