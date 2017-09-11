package de.tbressler.waterrower.io.msg.in;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.model.ModelInformation;

import static java.util.Objects.requireNonNull;

/**
 * Current Model Information (S4/S5 -> PC).
 *
 * Details of what unit is attached:
 * - Model - Sent as 4 or 5 to indicate if it is a Series 4 or series 5 rowing computer.
 * - Version high - 02 as an example for version 2.00 MSB of the firmware version.
 * - Version low - 00 as an example for version 2.00 LSB of the firmware version.
 *
 * [I][V] + [Model] + [Version High] + [Version Low] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ModelInformationMessage extends AbstractMessage {

    /* The model tzpe and firmware. */
    private final ModelInformation modelInformation;


    /**
     * Current model information.
     *
     * @param modelInformation The model tzpe and firmware, must not be null.
     */
    public ModelInformationMessage(ModelInformation modelInformation) {
        this.modelInformation = requireNonNull(modelInformation);
    }


    /**
     * Returns the model type and firmware.
     *
     * @return The model type and firmware, never null.
     */
    public ModelInformation getModelInformation() {
        return modelInformation;
    }

}
