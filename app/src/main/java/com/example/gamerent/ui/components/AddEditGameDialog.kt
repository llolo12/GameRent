package com.example.gamerent.ui.components

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
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.gamerent.model.Consolas
import com.example.gamerent.model.Generos
import com.example.gamerent.model.Videojuego
import java.io.File

private val MoradoColor = Color(0xFF8B72FF)
private val TextoSecundarioColor = Color(0xFFADB8CC)

@Composable
fun AddEditGameDialog(
    juegoEditar: Videojuego?,
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
    var consola by remember { mutableStateOf(juegoEditar?.consola ?: Consolas.PS5) }
    var genero by remember { mutableStateOf(juegoEditar?.genero ?: Generos.ACCION) }
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
            onError("No se pudo capturar la foto de la cámara.")
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
        title = { Text(if (juegoEditar == null) "Agregar Videojuego" else "Editar Videojuego") },
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
                    label = { Text("Nombre del videojuego *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Precio de Alquiler
                OutlinedTextField(
                    value = precioTexto,
                    onValueChange = { precioTexto = it },
                    label = { Text("Precio de alquiler por día ($)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Consola
                Text("Consola", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Consolas.opcionesFormulario) { opcion ->
                        val selected = consola.equals(opcion, ignoreCase = true)
                        FilterChip(
                            selected = selected,
                            onClick = { consola = opcion },
                            label = { Text(opcion) },
                            leadingIcon = if (selected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }

                // Género
                Text("Género", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Generos.opcionesFormulario) { opcion ->
                        val selected = genero.equals(opcion, ignoreCase = true)
                        FilterChip(
                            selected = selected,
                            onClick = { genero = opcion },
                            label = { Text(opcion) },
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
                    Text("Disponible para alquiler", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Switch(
                        checked = disponible,
                        onCheckedChange = { disponible = it }
                    )
                }

                // Selección de Imagen / Video
                Text("Imagen / Video del videojuego", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón Cámara
                    OutlinedButton(
                        onClick = {
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
                                onError("Error al iniciar la cámara: ${e.localizedMessage}")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Cámara", fontSize = 12.sp)
                    }

                    // Botón Galería Foto
                    OutlinedButton(
                        onClick = {
                            try {
                                galleryImageLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            } catch (e: Exception) {
                                onError("Error al abrir galería de fotos: ${e.localizedMessage}")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Galería", fontSize = 12.sp)
                    }

                    // Botón Galería Video
                    OutlinedButton(
                        onClick = {
                            try {
                                galleryVideoLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                )
                            } catch (e: Exception) {
                                onError("Error al abrir galería de video: ${e.localizedMessage}")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Video", fontSize = 12.sp)
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
                            contentDescription = "Vista previa multimedia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        if (mediaTypeState == "VIDEO") {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = "Video seleccionado",
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
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
