angular.module('todoApp2', []).controller('TodoController',
		[ '$scope', function($scope) {
			$scope.todos = [ {
				text : 'learn angular',
				done : true
			}, {
				text : 'build an angular app',
				done : false
			}, {
				text : 'toubidou',
				done : false
			} ];

			$scope.addTodo = function() {
				if (!$scope.todoText) {
					$scope.message = "You must provide text";
				} else {
					$scope.todos.push({
						text : $scope.todoText,
						done : false
					});
					$scope.message = "added " + $scope.todoText;
					$scope.todoText = "entrez la valeur ici";
				}
			};

			$scope.remaining = function() {
				var count = 0;
				angular.forEach($scope.todos, function(todo) {
					count += todo.done ? 0 : 1;
				});
				return count;
			};

			$scope.archive = function() {
				var oldTodos = $scope.todos;
				var i = 0;
				$scope.todos = [ {
					text : "salut mec",
					done : true
				} ];
				angular.forEach(oldTodos, function(todo) {
					if (!todo.done) {
						$scope.todos.push(todo);

					} else {
						i++;
					}
				});
				$scope.message = "archived " + i;
			};

			$scope.message = "chiotte les mecs";
		} ]);