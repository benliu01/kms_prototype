package com.blizzle.keys;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

public class DownloadObject {
	
	public static void main(String[] args) throws Exception {
		String accessKey = System.getenv("AWS_ACCESS_KEY");
		String secretKey = System.getenv("AWS_SECRET_KEY");
		String kms_cmk_id = System.getenv("KEY_ID");
		
		String bucketName = "ben-key-prototype"; 
		String objectKey  = "ExampleKMSEncryptedObject";

		KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kms_cmk_id);

		AmazonS3EncryptionClient encryptionClient = new AmazonS3EncryptionClient(new BasicAWSCredentials(accessKey, secretKey), materialProvider,
				new CryptoConfiguration().withKmsRegion(Regions.US_EAST_1))
		.withRegion(Region.getRegion(Regions.US_EAST_1));

		// Download the object.
		S3Object downloadedObject = encryptionClient.getObject(bucketName,
				objectKey);
		byte[] decrypted = IOUtils.toByteArray(downloadedObject
				.getObjectContent());
		
		System.out.println(new String(decrypted));
	}
}
