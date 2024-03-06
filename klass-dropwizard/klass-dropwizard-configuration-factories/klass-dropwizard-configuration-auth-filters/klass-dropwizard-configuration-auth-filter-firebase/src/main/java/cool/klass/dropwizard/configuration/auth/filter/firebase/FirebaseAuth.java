package cool.klass.dropwizard.configuration.auth.filter.firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseOptions.Builder;

public class FirebaseAuth
{
    private final String databaseUrl;
    private final String firebaseConfig;

    public FirebaseAuth(String databaseUrl, String firebaseConfig)
    {
        this.databaseUrl = Objects.requireNonNull(databaseUrl);
        this.firebaseConfig = Objects.requireNonNull(firebaseConfig);
    }

    public com.google.firebase.auth.FirebaseAuth getFirebaseAuth()
    {
        byte[]      bytes               = this.firebaseConfig.getBytes(StandardCharsets.UTF_8);
        InputStream firebaseCredentials = new ByteArrayInputStream(bytes);

        return this.getFirebaseAuth(firebaseCredentials);
    }

    private com.google.firebase.auth.FirebaseAuth getFirebaseAuth(@Nonnull InputStream firebaseCredentials)
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

    private com.google.firebase.auth.FirebaseAuth getFirebaseAuthNoThrow(@Nonnull InputStream firebaseCredentials) throws IOException
    {
        Objects.requireNonNull(firebaseCredentials);
        GoogleCredentials credentials = GoogleCredentials.fromStream(firebaseCredentials);
        FirebaseOptions options = new Builder()
                .setCredentials(credentials)
                .setDatabaseUrl(this.databaseUrl)
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        return com.google.firebase.auth.FirebaseAuth.getInstance(firebaseApp);
    }
}
