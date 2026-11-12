package com.example.practicadoskotlin.ui.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicadoskotlin.modelo.BebidaDTO
import com.example.practicadoskotlin.modelo.IngredienteDTO
import com.example.practicadoskotlin.modelo.PastaDTO
import com.example.practicadoskotlin.modelo.PizzaDTO
import com.example.practicadoskotlin.modelo.ProductoDTO
import com.example.practicadoskotlin.modelo.SIZE

private fun Double.euros(): String = "%.2f €".format(this)

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.productosAnadidos) {
        uiState.productosAnadidos?.let { n ->
            val texto = if (n == 1) "Se ha añadido 1 producto al carrito"
            else "Se han añadido $n productos al carrito"
            Toast.makeText(context, texto, Toast.LENGTH_SHORT).show()
            viewModel.onToastMostrado()
        }
    }

    if (uiState.cargando) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Catálogo de Productos", style = MaterialTheme.typography.titleLarge)
            CarritoIcono(uiState.totalProductos)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            seccion(TipoProducto.PIZZA, uiState.pizzas, viewModel::onCantidadChange,
                viewModel::onSizeChange, viewModel::onAnadirClick)
            seccion(TipoProducto.PASTA, uiState.pastas, viewModel::onCantidadChange,
                viewModel::onSizeChange, viewModel::onAnadirClick)
            seccion(TipoProducto.BEBIDA, uiState.bebidas, viewModel::onCantidadChange,
                viewModel::onSizeChange, viewModel::onAnadirClick)
        }
    }
}

@Composable
private fun CarritoIcono(total: Int) {
    BadgedBox(
        badge = { if (total > 0) Badge { Text("$total") } }
    ) {
        Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
    }
}

private fun LazyListScope.seccion(
    tipo: TipoProducto,
    lista: List<ItemCatalogo>,
    onCantidadChange: (Int, Int) -> Unit,
    onSizeChange: (Int, SIZE) -> Unit,
    onAnadirClick: (Int) -> Unit
) {
    if (lista.isEmpty()) return

    item { CabeceraSeccion(tipo, lista.size) }
    items(lista, key = { it.producto.id }) { item ->
        ProductoCard(
            item = item,
            onCantidadChange = { onCantidadChange(item.producto.id, it) },
            onSizeChange = { onSizeChange(item.producto.id, it) },
            onAnadirClick = { onAnadirClick(item.producto.id) }
        )
    }
}

// ---------- Elementos que diferencian los tipos ----------

@Composable
private fun CabeceraSeccion(tipo: TipoProducto, cantidad: Int) {
    val estilo = tipo.estilo()
    val fondo = MaterialTheme.colorScheme.primaryContainer
    val contenido = MaterialTheme.colorScheme.onPrimaryContainer
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = fondo,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(estilo.icono, contentDescription = null, tint = estilo.contenido)
            Text(
                tipo.titulo,
                style = MaterialTheme.typography.titleMedium,
                color = contenido
            )
            Spacer(Modifier.weight(1f))
            Text(
                if (cantidad == 1) "1 producto" else "$cantidad productos",
                style = MaterialTheme.typography.labelMedium,
                color = contenido
            )
        }
    }
}

@Composable
private fun ImagenProducto(tipo: TipoProducto) {
    val estilo = tipo.estilo()
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(estilo.fondo),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(estilo.imagen),
            contentDescription = "Imagen genérica de ${tipo.singular.lowercase()}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun EtiquetaTipo(tipo: TipoProducto) {
    val estilo = tipo.estilo()
    Surface(shape = RoundedCornerShape(6.dp), color = estilo.fondo) {
        Text(
            tipo.singular.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = estilo.contenido,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

// ---------- Tarjeta de producto ----------

@Composable
fun ProductoCard(
    item: ItemCatalogo,
    onCantidadChange: (Int) -> Unit,
    onSizeChange: (SIZE) -> Unit,
    onAnadirClick: () -> Unit
) {
    val tipo = item.producto.tipo()
    val estilo = tipo.estilo()

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, estilo.fondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Imagen + etiqueta + nombre + precio
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImagenProducto(tipo)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    EtiquetaTipo(tipo)
                    Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        item.precioUnitario.euros(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DetalleProducto(item.producto)

            // Selector de tamaño (con nombres propios de cada tipo)
            Text("Tamaño", style = MaterialTheme.typography.labelMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SIZE.entries.forEach { size ->
                    FilterChip(
                        selected = item.size == size,
                        onClick = { onSizeChange(size) },
                        label = { Text(size.etiquetaPara(tipo)) }
                    )
                }
            }

            // Selector de cantidad + subtotal
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onCantidadChange(item.cantidad - 1) },
                    enabled = item.cantidad > 1
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = "Quitar una unidad")
                }
                Text("${item.cantidad}", style = MaterialTheme.typography.titleMedium)
                IconButton(
                    onClick = { onCantidadChange(item.cantidad + 1) },
                    enabled = item.cantidad < 10
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir una unidad")
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "Subtotal: ${item.subtotal.euros()}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(
                onClick = onAnadirClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.AddShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Añadir al carrito")
            }
        }
    }
}

@Composable
private fun DetalleProducto(producto: ProductoDTO) {
    when (producto) {
        is PizzaDTO -> Ingredientes(producto.ingredientes)
        is PastaDTO -> Ingredientes(producto.ingredientes)
        is BebidaDTO -> Text("Bebida fría, se sirve en el tamaño elegido")
    }
}

@Composable
private fun Ingredientes(ingredientes: List<IngredienteDTO>) {
    Text("Ingredientes: ${ingredientes.joinToString(", ") { it.nombre }}")
    val alergenos = ingredientes.flatMap { it.alergenos }.distinct()
    if (alergenos.isNotEmpty()) {
        Text(
            "Alérgenos: ${alergenos.joinToString(", ")}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}