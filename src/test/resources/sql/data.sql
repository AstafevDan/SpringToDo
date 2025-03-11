INSERT INTO tasks (id, title, description, status, priority)
VALUES (1, 'task1', 'description1', 'PENDING', 'LOW'),
       (2, 'task2', 'description2', 'PENDING', 'MEDIUM'),
       (3, 'task3', 'description3', 'IN_PROGRESS', 'LOW'),
       (4, 'task4', 'description4', 'IN_PROGRESS', 'HIGH'),
       (5, 'task5', 'description5', 'COMPLETED', 'MEDIUM');
SELECT SETVAL('tasks_id_seq', (SELECT MAX(id) FROM tasks));