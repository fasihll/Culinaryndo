# Culinaryndo

## Setup Project
Add TFLite Model with Android Studio
```bash
app -> new -> other -> TensorFlow Lite Model
```
[TFLite Model Download](https://www.kaggle.com/models/achfasihullisan/tflite_10_taditional-food_indoensia)

Change filename in ui/scan/ImageClassifierHelpet.kt
```bash
val modelName: String = "filename.tflite",
```

## Setup [API](https://github.com/fasihll/culinaryndo-web) Config in Data/Network/ApiConfig

Local with terminal
```bash
http://10.0.2.2:{Your Port}/
```

Portable Hostpot
```bash
http://{ipv4 Addresss}:{Your Port}/
```
Note: private wifi, disable firewall

Hosting
```bash
http://Culinaryndo.com/
```

## Usage
```bash
val retrofit = Retrofit.Builder()
                .baseUrl("http://Culinaryndo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
```
