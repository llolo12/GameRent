package com.example.gamerent.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.gamerent.domain.model.Consoles
import com.example.gamerent.domain.model.Genres
import com.example.gamerent.domain.model.VideoGame
import com.example.gamerent.R
import java.io.File

private val MoradoColor = Color(0xFF8B72FF)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun AddEditGameDialog(
    juegoEditar: VideoGame?,
    onDismiss: () -> Unit,
    onSave: (
        nombre: String,
        consola: String,
        genero: String,
        disponible: Boolean,
        mediaUri: String?,
        mediaType: String?,
        precio: Double
    ) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf(juegoEditar?.nombre ?: "") }
    var consola by remember { mutableStateOf(juegoEditar?.consola ?: Consoles.PS5) }
    var genero by remember { mutableStateOf(juegoEditar?.genero ?: Genres.ACCION) }
    var disponible by remember { mutableStateOf(juegoEditar?.disponible ?: true) }
    var precioTexto by remember { mutableStateOf((juegoEditar?.precioAlquiler ?: 5.0).toString()) }
    var mediaUriState by remember { mutableStateOf(juegoEditar?.mediaUri) }
    var mediaTypeState by remember { mutableStateOf(juegoEditar?.mediaType) }

    // Temp Uri for Camera Photo capture
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher for Camera Photo
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            mediaUriState = tempCameraUri.toString()
            mediaTypeState = "IMAGE"
        } else {
            onError(context.getString(R.string.camera_capture_error))
        }
    }

    fun launchCamera() {
        try {
            val tempFile = File.createTempFile("game_photo_", ".jpg", context.cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                tempFile
            )
            tempCameraUri = uri
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            onError(context.getString(R.string.camera_start_error, e.localizedMessage.orEmpty()))
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            onError(context.getString(R.string.camera_permission_denied))
        }
    }

    // Launcher for Gallery Image Pick
    val galleryImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            mediaUriState = uri.toString()
            mediaTypeState = "IMAGE"
        }
    }

    // Launcher for Gallery Video Pick
    val galleryVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            mediaUriState = uri.toString()
            mediaTypeState = "VIDEO"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(if (juegoEditar == null) R.string.add_game_dialog else R.string.edit_game_dialog)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(stringResource(R.string.game_name_required)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Precio de Alquiler
                OutlinedTextField(
                    value = precioTexto,
                    onValueChange = { precioTexto = it },
                    label = { Text(stringResource(R.string.rental_price_per_day)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Consola
                Text(stringResource(R.string.console_label), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Consoles.opcionesFormulario) { opcion ->
                        val selected = consola.equals(opcion, ignoreCase = true)
                        FilterChip(
                            selected = selected,
                            onClick = { consola = opcion },
                            label = { Text(LocalizedGameOptionLabel(opcion)) },
                            leadingIcon = if (selected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }

                // Género
                Text(stringResource(R.string.genre_label), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Genres.opcionesFormulario) { opcion ->
                        val selected = genero.equals(opcion, ignoreCase = true)
                        FilterChip(
                            selected = selected,
                            onClick = { genero = opcion },
                            label = { Text(LocalizedGameOptionLabel(opcion)) },
                            leadingIcon = if (selected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }

                // Disponibilidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.rental_available), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it }
                    )
                }

                // Selección de Imagen / Video
                Text(stringResource(R.string.image_video_game), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Cámara
                    OutlinedButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                launchCamera()
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.camera), fontSize = 12.sp)
                    }

                    // Botón Galería Foto
                    OutlinedButton(
                        onClick = {
                            try {
                                galleryImageLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            } catch (e: Exception) {
                                onError(context.getString(R.string.gallery_photo_error, e.localizedMessage.orEmpty()))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.gallery), fontSize = 12.sp)
                    }

                    // Botón Galería Video
                    OutlinedButton(
                        onClick = {
                            try {
                                galleryVideoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            } catch (e: Exception) {
                                onError(context.getString(R.string.gallery_video_error, e.localizedMessage.orEmpty()))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.video), fontSize = 12.sp)
                    }
                }

                // Vista previa de la multimedia seleccionada
                mediaUriState?.let { uriStr ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = uriStr,
                            contentDescription = stringResource(R.string.photo_preview_accessibility),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (mediaTypeState == "VIDEO") {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = stringResource(R.string.selected_video_accessibility),
                                tint = MoradoColor,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val precioNum = precioTexto.toDoubleOrNull() ?: 5.0
                    onSave(
                        nombre,
                        consola,
                        genero,
                        disponible,
                        mediaUriState,
                        mediaTypeState,
                        precioNum
                    )
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
