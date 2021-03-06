package pl.edu.pjwstk.lab02.app;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import pl.edu.pjwstk.lab02.messenger.ConnectionStatus;
import pl.edu.pjwstk.lab02.messenger.MalformedRecipientException;
import pl.edu.pjwstk.lab02.messenger.MessageService;
import pl.edu.pjwstk.lab02.messenger.MessageServiceSimpleImpl;
import pl.edu.pjwstk.lab02.messenger.SendingStatus;



public class MockitoTest {

	private static final String VALID_SERVER = "inf.ug.edu.pl";
    private static final String INVALID_SERVER = "inf.ug.edu.eu";

    private static final String VALID_MESSAGE = "some message";
    private static final String INVALID_MESSAGE = "ab";

    @Mock
    MessageService serviceMock;

    @Rule 
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    
    @InjectMocks
    Messenger app = new Messenger(new MessageServiceSimpleImpl());
    

    @Test
    public void sendMessageTest() throws MalformedRecipientException {
        when(serviceMock.send(INVALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENDING_ERROR);
        when(serviceMock.send(VALID_SERVER, VALID_MESSAGE)).thenReturn(SendingStatus.SENT);
        doThrow(new MalformedRecipientException()).when(serviceMock).send(VALID_SERVER, INVALID_MESSAGE);
        assertThat(app.sendMessage(VALID_SERVER, VALID_MESSAGE), equalTo(0));
        assertThat(app.sendMessage(INVALID_SERVER, VALID_MESSAGE), equalTo(1));
        assertThat(app.sendMessage(VALID_SERVER, INVALID_MESSAGE), equalTo(2));
    }

    @Test
    public void testConnectionTest() {
        when(serviceMock.checkConnection(VALID_SERVER)).thenReturn(ConnectionStatus.SUCCESS);
        when(serviceMock.checkConnection(INVALID_SERVER)).thenReturn(ConnectionStatus.FAILURE);
        assertThat(app.testConnection(VALID_SERVER), equalTo(0));
        assertThat(app.testConnection(INVALID_SERVER), equalTo(1));
    }
}
