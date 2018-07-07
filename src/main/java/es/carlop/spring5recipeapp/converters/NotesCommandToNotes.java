package es.carlop.spring5recipeapp.converters;

import es.carlop.spring5recipeapp.commands.NotesCommand;
import es.carlop.spring5recipeapp.domain.Notes;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Notes> {

    @Synchronized
    @Nullable
    @Override
    public Notes convert(NotesCommand source) {
        if (source == null) {
            return null;
        }

        final Notes note = new Notes();
        note.setId(source.getId());
        note.setRecipeNotes(source.getRecipeNotes());

        return note;
    }
}
