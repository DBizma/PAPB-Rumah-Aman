const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendTipNotification = functions.firestore
  .document("tips/{tipId}")
  .onCreate(async (snapshot, context) => {
    const tipData = snapshot.data();
    const tipTitle = tipData.title;
    const tipBody = tipData.description;

    const payload = {
      notification: {
        title: `💡 Tips Baru: ${tipTitle}`,
        body: tipBody,
      },
      topic: "new_tips",
    };

    console.log("Mempersiapkan notifikasi untuk dikirim:", payload);

    try {
      const response = await admin.messaging().send(payload);
      console.log("Notifikasi berhasil dikirim:", response);
      return response;
    } catch (error) {
      console.error("Gagal mengirim notifikasi:", error);
      return null;
    }
  });
