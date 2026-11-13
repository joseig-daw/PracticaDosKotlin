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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

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
            modifier = Modifier.weight(1f),
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

    item(key = "cabecera-${tipo.name}") {
        Box(Modifier.padding(horizontal = 16.dp)) {
            CabeceraSeccion(tipo, lista.size)
        }
    }

    item(key = "fila-${tipo.name}") {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lista, key = { it.producto.id }) { item ->
                ProductoCard(
                    item = item,
                    onCantidadChange = { onCantidadChange(item.producto.id, it) },
                    onSizeChange = { onSizeChange(item.producto.id, it) },
                    onAnadirClick = { onAnadirClick(item.producto.id) },
                    modifier = Modifier.width(320.dp)
                )
            }
        }
    }
}

// ---------- Elementos que diferencian los tipos ----------

@Composable
private fun CabeceraSeccion(tipo: TipoProducto, cantidad: Int) {
    val estilo = tipo.estilo()
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icono en círculo
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colores.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(estilo.icono, contentDescription = null, tint = colores.onPrimary)
        }

        // Título y contador
        Column(modifier = Modifier.weight(1f)) {
            Text(
                tipo.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (cantidad == 1) "1 producto" else "$cantidad productos",
                style = MaterialTheme.typography.bodySmall,
                color = colores.onSurfaceVariant
            )
        }

        // Pista de desplazamiento (solo si hay más de una tarjeta)
        if (cantidad > 1) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Desliza",
                    style = MaterialTheme.typography.labelMedium,
                    color = colores.primary
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colores.primary
                )
            }
        }
    }
}

@Composable
fun ProductoCard(
    item: ItemCatalogo,
    onCantidadChange: (Int) -> Unit,
    onSizeChange: (SIZE) -> Unit,
    onAnadirClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tipo = item.producto.tipo()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        HeroProducto(tipo, item.precioUnitario)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                item.producto.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            InfoProducto(item.producto)

            SelectorTamano(tipo, item.size, onSizeChange)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SelectorCantidad(item.cantidad, onCantidadChange)

                Button(
                    onClick = onAnadirClick,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(Icons.Filled.AddShoppingCart, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir")
                }
            }
        }
    }
}

// Zona superior: imagen + etiqueta de tipo + precio
@Composable
private fun HeroProducto(tipo: TipoProducto, precio: Double) {
    val estilo = tipo.estilo()
    val colores = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                Brush.verticalGradient(
                    listOf(colores.primaryContainer, colores.primaryContainer.copy(alpha = 0.55f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(estilo.imagen),
            contentDescription = "Imagen genérica de ${tipo.singular.lowercase()}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )

        // Etiqueta del tipo, arriba a la izquierda
        Surface(
            shape = RoundedCornerShape(50),
            color = colores.surface.copy(alpha = 0.85f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    estilo.icono,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = colores.primary
                )
                Text(tipo.singular, style = MaterialTheme.typography.labelMedium)
            }
        }

        // Precio, en la esquina inferior derecha
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp),
            color = colores.primary,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text(
                precio.euros(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colores.onPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

// Descripción: ingredientes y alérgenos (o texto de bebida)
@Composable
private fun InfoProducto(producto: ProductoDTO) {
    val ingredientes = when (producto) {
        is PizzaDTO -> producto.ingredientes
        is PastaDTO -> producto.ingredientes
        is BebidaDTO -> null
    }

    if (ingredientes == null) {
        Text(
            "Bebida fría. Se sirve en el tamaño elegido.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            minLines = 2,
            maxLines = 2
        )
        return
    }

    Text(
        ingredientes.joinToString(" · ") { it.nombre },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        minLines = 2,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

    val alergenos = ingredientes.flatMap { it.alergenos }.distinct()
    if (alergenos.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                "Alérgenos: ${alergenos.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Selector de tamaño estilo "segmentado"
@Composable
private fun SelectorTamano(
    tipo: TipoProducto,
    seleccionado: SIZE,
    onSizeChange: (SIZE) -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colores.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SIZE.entries.forEach { size ->
            val activo = size == seleccionado
            val fondo by animateColorAsState(
                if (activo) colores.primary else Color.Transparent,
                label = "fondoTamano"
            )
            val texto by animateColorAsState(
                if (activo) colores.onPrimary else colores.onSurfaceVariant,
                label = "textoTamano"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(fondo)
                    .selectable(
                        selected = activo,
                        onClick = { onSizeChange(size) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    size.etiquetaPara(tipo),
                    style = MaterialTheme.typography.labelMedium,
                    color = texto,
                    fontWeight = if (activo) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Selector de cantidad en forma de píldora
@Composable
private fun SelectorCantidad(cantidad: Int, onCantidadChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledTonalIconButton(
            onClick = { onCantidadChange(cantidad - 1) },
            enabled = cantidad > 1,
            modifier = Modifier.size(34.dp)
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Quitar una unidad", modifier = Modifier.size(18.dp))
        }
        Text(
            "$cantidad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 32.dp)
        )
        FilledTonalIconButton(
            onClick = { onCantidadChange(cantidad + 1) },
            enabled = cantidad < 10,
            modifier = Modifier.size(34.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Añadir una unidad", modifier = Modifier.size(18.dp))
        }
    }
}