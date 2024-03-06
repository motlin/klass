package cool.klass.dropwizard.auth.filter.provider.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseOptions.Builder;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthFactory
{
    private final String firebaseCredentialsClasspathLocation;
    private final String databaseUrl;

    public FirebaseAuthFactory(String firebaseCredentialsClasspathLocation, String databaseUrl)
    {
        this.firebaseCredentialsClasspathLocation = Objects.requireNonNull(firebaseCredentialsClasspathLocation);
        this.databaseUrl = Objects.requireNonNull(databaseUrl);
    }

    public FirebaseAuth getFirebaseAuth()
    {
        InputStream firebaseCredentials = FirebaseAuthFactory.class.getResourceAsStream(
                this.firebaseCredentialsClasspathLocation);
        return this.getFirebaseAuth(firebaseCredentials);
    }

    private FirebaseAuth getFirebaseAuth(@Nonnull InputStream firebaseCredentials)
    {
        try
        {
            return this.getFirebaseAuthNoThrow(firebaseCredentials);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private FirebaseAuth getFirebaseAuthNoThrow(@Nonnull InputStream firebaseCredentials) throws IOException
    {
        Objects.requireNonNull(firebaseCredentials);
        GoogleCredentials credentials = GoogleCredentials.fromStream(firebaseCredentials);
        FirebaseOptions options = new Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(this.databaseUrl)
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
