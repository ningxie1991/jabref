package org.jabref.gui.importer;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableBooleanValue;
import org.jabref.gui.DialogService;
import org.jabref.gui.JabRefFrame;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.gui.util.OptionalObjectProperty;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.types.EntryType;
import org.jabref.preferences.PreferencesService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewEntryActionTest {

  private final JabRefFrame jabRefFrame = mock(JabRefFrame.class);
  private final DialogService dialogService = mock(DialogService.class);
  private final PreferencesService preferences = mock(PreferencesService.class);
  private final StateManager stateManager = mock(StateManager.class);
  private final LibraryTab libraryTab = mock(LibraryTab.class);

  @Test
  void noNewEntryActionWhenNoDatabaseIsOpen() {
    OptionalObjectProperty<BibDatabaseContext> activeDatabase = mock(OptionalObjectProperty.class);
    BooleanExpression trueBool = mock(BooleanExpression.class);
    when(trueBool.getValue()).thenReturn(true);
    when(activeDatabase.isPresent()).thenReturn(trueBool);
    when(jabRefFrame.getBasePanelCount()).thenReturn(0);
    when(jabRefFrame.getCurrentLibraryTab()).thenReturn(libraryTab);
    when(stateManager.activeDatabaseProperty()).thenReturn(activeDatabase);

    NewEntryAction newEntryAction = new NewEntryAction(jabRefFrame, dialogService, preferences, stateManager);

    newEntryAction.execute();

    verify(libraryTab, never()).insertEntry(any());
  }
}
