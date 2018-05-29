var product = $scope.product = [];															// Custom JavaScript creates a JavaScript Object and binds it to the current AngularJS $scope of the form as a variable named "product".															                                                        
	$scope.addProduct = function () {														// We make a function named "addProduct".
		var product = {};																	// We add a new "product" to the Array.
		product.Category = $scope.Category;
		product.Description = $scope.Description;
		if (!!$scope.camForm.fields[0].element[0].files[0]) {
			product.Details = $scope.camForm.fields[0].element[0].files[0].name;			// We check if the file is uploaded.
		} else {
				return;																		// If the file is not uploaded, it returns "undefined".
		}
		product.Price = $scope.Price;
		$scope.product.push(product);														// We use the value of the "product" input field to add a new "product" to the Array.
		$scope.Category = "";																// We clear the TextBox "Κατηγορία".
		$scope.Description = "";															// We clear the TextBox "Περιγραφή".
		$scope.Details = "";																// We clear the TextBox "Λεπτομέρειες".
		$scope.Price = "";																	// We clear the TextBox "Τιμή (€)".
	};
	$scope.removeProduct = function (index) {												// We make a function named "removeProduct". 					
		var category = $scope.product[index].Category;										// We find product's Category using "index" from the Array and binds it to the current AngularJS $scope of the form as a variable named "category".
		$scope.product.splice(index, 1);													// We use an index to remove a "product" from the Array.
	}
	$scope.isAddFormValid = function () {													// We make a function named "isAddFormValid".
		return ($scope.Category &&
				$scope.Description &&
				$scope.camForm.fields[0].element[0].files[0] &&
				$scope.Price) ? true : false;												// If all of the 4 parameters of variable "product" are added, the value will be "true", otherwise the value will be "false".
	}
	camForm.on('form-loaded', function() {													// We hook into the lifecycle of Camunda SDK JS Form.
		camForm.variableManager.createVariable ({											// We "create" (declare) a new process variable  
				name:'product',																// named 'product' and
				type:'json',																// provide as type information 'json' used for serialization.
				value:product
			});
	});
	camForm.on('submit', function(evt) {													// We hook into the lifecycle of Camunda SDK JS Form.
		if (product.length<1) {																// If no any "product" is added,
			evt.submitPrevented = true;														// an event handler prevents the form from being submitted by setting the property "submitPrevented" to 'true'.
		}
	});