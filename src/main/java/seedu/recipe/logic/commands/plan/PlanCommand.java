package seedu.recipe.logic.commands.plan;

import static java.util.Objects.requireNonNull;
import static seedu.recipe.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.recipe.model.Model.PREDICATE_SHOW_ALL_PLANNED_RECIPES;

import java.util.ArrayList;
import java.util.List;

import seedu.recipe.commons.core.Messages;
import seedu.recipe.commons.core.index.Index;
import seedu.recipe.logic.commands.Command;
import seedu.recipe.logic.commands.CommandResult;
import seedu.recipe.logic.commands.exceptions.CommandException;
import seedu.recipe.model.Model;
import seedu.recipe.model.plan.PlannedDate;
import seedu.recipe.model.plan.PlannedRecipe;
import seedu.recipe.model.recipe.Recipe;
import seedu.recipe.ui.tab.Tab;

/**
 * Schedules a recipe to a date.
 */
public class PlanCommand extends Command {

    public static final String COMMAND_WORD = "plan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Plans a recipe to be cooked on a certain date. "
            + "Parameters: "
            + "RECIPE_INDEX "
            + PREFIX_DATE + "YYYY-MM-DD \n"
            + "Example: " + COMMAND_WORD + " "
            + "3 "
            + PREFIX_DATE + "2020-03-16";

    public static final String MESSAGE_SUCCESS = "Recipe %1$s planned at %2$s";

    private final Index index;
    private final PlannedDate atDate;
    private final Tab planTab = Tab.PLANNING;

    /**
     * Creates an PlanCommand to set the specified {@code Recipe} on a certain date
     */
    public PlanCommand(Index index, PlannedDate date) {
        requireNonNull(index);
        requireNonNull(date);
        this.index = index;
        atDate = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToPlan = lastShownList.get(index.getZeroBased());
        List<Recipe> recipesToPlan = new ArrayList<>();
        recipesToPlan.add(recipeToPlan);

        PlannedRecipe plannedRecipe = new PlannedRecipe(recipesToPlan, atDate);

        model.addPlannedRecipe(plannedRecipe);
        model.addPlannedMapping(recipeToPlan, plannedRecipe);
        model.updateFilteredPlannedList(PREDICATE_SHOW_ALL_PLANNED_RECIPES);
        model.commitRecipeBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, recipeToPlan.toString(), atDate.toString()),
                false, planTab, false);
    }

}