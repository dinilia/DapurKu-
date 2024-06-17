package org.d3if3119.dapurku.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3119.dapurku.BuildConfig
import org.d3if3119.dapurku.R
import org.d3if3119.dapurku.model.Resep
import org.d3if3119.dapurku.model.User
import org.d3if3119.dapurku.network.ApiStatus
import org.d3if3119.dapurku.network.UserDataStore
import org.d3if3119.dapurku.ui.theme.DapurKuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val notLoggedIn by remember { mutableStateOf(user.email == "") }
    var showDialog by remember { mutableStateOf(false) }
    var showImgDialog by remember { mutableStateOf(false) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showImgDialog = true
    }

    val isSuccess by viewModel.querySuccess

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Berhasil!", Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }


    val data by viewModel.data
    Log.d("MainVM", "data: $data")
    val status by viewModel.status.collectAsState()
    var showHapusDialog by remember { mutableStateOf(false) }
    var recipeData by remember { mutableStateOf<Resep?>(null) }


    LaunchedEffect(user.email) {
        viewModel.retrieveData(user.email)
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Log.d("MainScreen", "$errorMessage")
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dapurku!", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        } else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.account_circle),
                            contentDescription = "Profil",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (user.email.isNotEmpty() && user.email != "") {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = true,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                } else {
                    Toast.makeText(context, "Anda belum login :(", Toast.LENGTH_SHORT).show()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah"
                )
            }
        }
    ) {
        if (showDialog) {
            ProfilDialog(user = user, onDismissRequest = { showDialog = false }) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }

        if (showImgDialog) {
            ImageDialog(
                bitmap = bitmap,
                onDismissRequest = { showImgDialog = false }) { nama, deskripsi ->
                viewModel.saveData(user.email, nama, deskripsi, bitmap!!)
                showImgDialog = false
            }
        }

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (!notLoggedIn) {
                        Text(text = "Masak apa hari ini, ")
                        Text(
                            text = "Sendiko",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    } else {
                        Text(
                            text = "Masak apa hari ini?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                }
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                SearchBar(
                    value = "",
                    onSearchAction = {},
                    filterList = listOf("Sup", "Gorengan", "Minuman Kopi"),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "Resep Makanan Indonesia",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            when (status) {
                ApiStatus.SUCCESS -> {
                    items(data) { recipe ->
                        RecipeCard(
                            name = recipe.nama,
                            imageUrl = recipe.image_id,
                            deskripsi = recipe.deskripsi,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            recipeData = recipe
                            showHapusDialog = true
                        }
                    }
                    item {
                        if (showHapusDialog) {
                            HapusDialog(data = recipeData!!, onDismissRequest = { showHapusDialog = false }) {
                                viewModel.deleteData(user.email, recipeData!!.recipe_id, recipeData!!.delete_hash)
                                showHapusDialog = false
                            }
                        }
                    }
                }

                ApiStatus.LOADING -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                ApiStatus.FAILED -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (user.email.isEmpty()) {
                                Text(text = "login dulu yuk untuk melihat data!")
                            } else {
                                Text(text = "Data kosong atau terjadi kesalahan.")
                                Button(
                                    onClick = { viewModel.retrieveData(user.email) },
                                    modifier = Modifier.padding(top = 16.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 32.dp,
                                        vertical = 16.dp
                                    )
                                ) {
                                    Text(text = "Coba lagi")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private suspend fun signIn(
    context: Context,
    dataStore: UserDataStore
) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
    ) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}


@Preview
@Composable
private fun HomeScreenPrev() {
    DapurKuTheme {
        HomeScreen()
    }
}