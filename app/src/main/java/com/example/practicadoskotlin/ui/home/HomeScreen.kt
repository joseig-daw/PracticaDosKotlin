package com.example.practicadoskotlin.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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

    if (uiState.cargando) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Catálogo de Productos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        seccion("Pizzas", uiState.pizzas, viewModel::onCantidadChange, viewModel::onSizeChange)
        seccion("Pastas", uiState.pastas, viewModel::onCantidadChange, viewModel::onSizeChange)
        seccion("Bebidas", uiState.bebidas, viewModel::onCantidadChange, viewModel::onSizeChange)
    }
}

private fun LazyListScope.seccion(
    titulo: String,
    lista: List<ItemCatalogo>,
    onCantidadChange: (Int, Int) -> Unit,
    onSizeChange: (Int, SIZE) -> Unit
) {
    if (lista.isEmpty()) return

    item { Text(titulo, style = MaterialTheme.typography.titleMedium) }
    items(lista, key = { it.producto.id }) { item ->
        ProductoCard(
            item = item,
            onCantidadChange = { onCantidadChange(item.producto.id, it) },
            onSizeChange = { onSizeChange(item.producto.id, it) }
        )
    }
}

@Composable
fun ProductoCard(
    item: ItemCatalogo,
    onCantidadChange: (Int) -> Unit,
    onSizeChange: (SIZE) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    item.precioUnitario.euros(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            DetalleProducto(item.producto)

            // Selector de tamaño
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SIZE.entries.forEach { size ->
                    FilterChip(
                        selected = item.size == size,
                        onClick = { onSizeChange(size) },
                        label = { Text(size.etiqueta()) }
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
                Text(
                    "Subtotal: ${item.subtotal.euros()}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun DetalleProducto(producto: ProductoDTO) {
    when (producto) {
        is PizzaDTO -> {
            Text("Pizza", style = MaterialTheme.typography.labelLarge)
            Ingredientes(producto.ingredientes)
        }
        is PastaDTO -> {
            Text("Pasta", style = MaterialTheme.typography.labelLarge)
            Ingredientes(producto.ingredientes)
        }
        is BebidaDTO -> {
            Text("Bebida", style = MaterialTheme.typography.labelLarge)
            Text("Se sirve en el tamaño elegido")
        }
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