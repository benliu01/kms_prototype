package com.blizzle.keys;

import java.io.ByteArrayInputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class UploadObject {

	public static void main(String[] args) throws Exception {
		String accessKey = System.getenv("AWS_ACCESS_KEY");
		String secretKey = System.getenv("AWS_SECRET_KEY");
		
		String bucketName = "ben-key-prototype"; 
		String objectKey  = "ExampleKMSEncryptedObject";
		String kms_cmk_id = "69f90dc8-ad73-43d1-825a-ed7a8ec5c486";

		KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kms_cmk_id);

		AmazonS3EncryptionClient encryptionClient = new AmazonS3EncryptionClient(new BasicAWSCredentials(accessKey, secretKey), materialProvider,
				new CryptoConfiguration().withKmsRegion(Regions.US_EAST_1))
		.withRegion(Region.getRegion(Regions.US_EAST_1));

		String lyrics  = "He said the way my blue eyes shined\n"
							+ "Put those Georgia stars to shame that night\n"
							+ "I said, That's a lie.\n"
							+ "Just a boy in a Chevy truck\n"
							+ "That had a tendency of gettin' stuck\n"
							+ "On back roads at night\n"
							+ "And I was right there beside him all summer long\n"
							+ "And then the time we woke up to find that summer gone".getBytes();
		byte[] plaintext = lyrics.getBytes();
		
		encryptionClient.putObject(new PutObjectRequest(bucketName, objectKey,
				new ByteArrayInputStream(plaintext), new ObjectMetadata()));

		System.out.println("Uploaded lyrics...");
	}
}
