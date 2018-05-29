camForm.on('form-loaded', function() {																					// The variable 'product' has not been loaded ("fetched") from the server yet.
		camForm.variableManager.fetchVariable('product');																// We tell the "Form SDK" to "fetch" the 'json' variable named 'product'.
});
camForm.on('variables-fetched', function() {																			// The variable 'product' has been loaded ("fetched") from the server
		$scope.product = camForm.variableManager.variableValue('product');												// and is bound to the current AngularJS $scope of the form.
});
$scope.changeQuantity = function(x, index, chkselct) {																	// "changeQuantity" function gets called at every change in the value of ("checkbox" or "Ποσότητα") input fields. 
		console.log('x: ' +x.Quantity+ ', chkselct: ' +chkselct+ ', index: '+ index);									// We display (x, chkselct, index) values in the debugger window. 
		if (!chkselct) {																								// If product's "checkbox" is not checked, then
			x.Quantity=undefined;																						// product's "Ποσότητα" input field has not a value.
		}
		$scope.product[index] = x;																						// Using index from the Array, we find the product, whose ("checkbox" or "Ποσότητα" input fields) value has changed.  
		$scope.total = total();																							// "total" is bound to the current AngularJS $scope of the form as a function which gets called.
}
function total() {																										// When "total ()" function gets called,
var total = 0;																											// variable "total" is initialized.
angular.forEach($scope.product, function(x) {																			// Based on the index from the Array, "forEach" function reads each product whose ("checkbox" or "Ποσότητα" input fields) value has changed.
		if (x.Quantity) { 																								// If product's "Ποσότητα" input field has a value (so, product's "checkbox" is checked), then
			total += x.Price * x.Quantity;																				// we calculate "total" (by multiplying "product's price" by "product's quantity" and adding the last "total").												
		}
})
	console.log('Total: ' + total);																						// We display "total" value in the debugger window.
	return total;																										// "return" statement stops the execution of "total ()" function and returns a value from that function.
}
camForm.on('submit', function(evt) {																					// Before "submit" request is sent to the server,
		var selectedProduct = [];																						// We create a new Array as a variable named "selectedProduct".
		angular.forEach($scope.product, function(x) {																	// For each product of the "product" Array 
			if (x.Quantity) {																							// if product's "Ποσότητα" input field has a value (so, product's "checkbox" is checked),
				selectedProduct.push(x);																				// we add the selected product to the "selectedProduct" Array.	
			}
		})
		if (selectedProduct.length<1) {																					// If no "product" has been selected from the Table,
			evt.submitPrevented = true;																					// an event handler prevents the form from being submitted by setting the property "submitPrevented" to 'true'.
		} else {																										// If at least one "product" has been selected from the Table, 
				camForm.variableManager.createVariable ({																// we "create" (declare) a new process variable
					name: 'selectedProduct',																			// named 'selectedProduct' and
					type: 'json',																						// provide as type information 'json' used for serialization.
					value: selectedProduct																					 
				});	
		}
});